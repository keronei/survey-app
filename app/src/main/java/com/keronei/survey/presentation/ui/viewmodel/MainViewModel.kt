/*
 * Copyright 2022 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.survey.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keronei.survey.core.QuestionnaireController
import com.keronei.survey.core.Resource
import com.keronei.survey.core.ViewState
import com.keronei.survey.domain.mapper.QuestionnaireDefToPresentationMapper
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val questionnaireRepository: QuestionnaireRepository) :
    ViewModel() {

    private val currentQuestionnaireController = QuestionnaireController

    private val questionnaires: MutableStateFlow<ViewState> = MutableStateFlow(value = ViewState.Empty)

    var selectedQuestionnaireToFill: QuestionnaireDef? = null
        private set

    fun getAvailableQuestionnaires(): Flow<ViewState> {
        viewModelScope.launch {
            questionnaireRepository.getQuestionnaires().collect { list ->
                when (list) {
                    is Resource.Failure -> {
                        questionnaires.emit(ViewState.Error(list.exception))
                    }
                    Resource.Loading -> {
                        questionnaires.emit(ViewState.Loading)
                    }
                    is Resource.Success -> {
                        if (list.data.isEmpty()) {
                            questionnaires.emit(ViewState.Empty)
                        } else {
                            questionnaires.emit(
                                ViewState.Success(
                                    QuestionnaireDefToPresentationMapper().mapList(
                                        list.data
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        return questionnaires
    }

    fun setSelectedQuestionnaire(selectedQuestionnaire: QuestionnaireDef) {
        currentQuestionnaireController.setupController(selectedQuestionnaire)
        selectedQuestionnaireToFill = selectedQuestionnaire
    }

    fun getCurrentQuestion() = currentQuestionnaireController.getCurrentQuestion()

    fun nextQuestion(): QuestionDefinition? =
        currentQuestionnaireController.getNextQuestion()

    fun previousQuestion() = currentQuestionnaireController.getPreviousQuestion()

    fun getCurrentEvent() = currentQuestionnaireController.getCurrentEvent()

    fun saveCurrentAnswer(questionDefinition: QuestionDefinition) =
        currentQuestionnaireController.saveQuestionResponse(questionDefinition)

    fun getQuestionnaireById(string: String) = questionnaireRepository.getQuestionnaireById(string)
}