package com.keronei.survey.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keronei.survey.core.QuestionnaireController
import com.keronei.survey.domain.mapper.QuestionnaireDefToPresentationMapper
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import com.keronei.survey.presentation.models.QuestionnaireDefPresentation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@ExperimentalCoroutinesApi
class MainViewModel(private val questionnaireRepository: QuestionnaireRepository) : ViewModel() {

    private val currentQuestionnaireController = QuestionnaireController

    var selectedQuestionnaireToFill: QuestionnaireDef? = null
        private set

    fun getAvailableQuestionnaires(): Flow<List<QuestionnaireDefPresentation>> =
        questionnaireRepository.getQuestionnaires().mapLatest{ list -> QuestionnaireDefToPresentationMapper().mapList(list) }

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

    fun saveQuestionnaireResponses() {

    }

}