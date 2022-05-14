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
import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.SubmissionViewState
import com.keronei.survey.domain.repositories.SubmissionsRepository
import com.keronei.survey.presentation.models.SubmissionPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SubmissionsViewModel @Inject constructor(private val submissionsRepository: SubmissionsRepository) :
    ViewModel() {
    private val submissions: MutableStateFlow<SubmissionViewState> =
        MutableStateFlow(value = SubmissionViewState.Empty)

    val submissionsStateFlow: StateFlow<SubmissionViewState> = submissions

    private val parser = SimpleDateFormat("dd.MM.yyyy hh:mm a", Locale.US)

    fun getSubmissions() {
        viewModelScope.launch {
            submissionsRepository.getSubmissions().collect { sub ->
                if (sub.isEmpty()) {
                    submissions.emit(SubmissionViewState.Empty)
                } else {
                    submissions.emit(
                        SubmissionViewState.Success(
                            sub.map { entry ->
                                SubmissionPresentation(
                                    entry.id.toString(),
                                    parser.format(Date(entry.submissionDate)),
                                    entry.submissionName,
                                    entry.questionnaireId,
                                    entry.synced
                                )
                            }
                        )
                    )
                }
            }
        }
    }

    fun saveQuestionnaireResponses(id: String, name: String, answers: List<AnswerData>) {
        viewModelScope.launch {
            submissionsRepository.saveQuestionnaireResponse(id, name, answers)
        }
    }
}