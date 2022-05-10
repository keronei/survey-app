package com.keronei.survey.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keronei.survey.core.QuestionnaireController
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository

class MainViewModel(private val questionnaireRepository: QuestionnaireRepository) : ViewModel() {

    private val currentQuestionnaireController = QuestionnaireController

    fun getAvailableQuestionnaires(): List<QuestionnaireDef> =
        questionnaireRepository.getQuestionnaires()

    fun setSelectedQuestionnaire(selectedQuestionnaire: QuestionnaireDef) {
        val questionnaire = currentQuestionnaireController.setupController(selectedQuestionnaire)
    }

    fun getCurrentQuestion() = currentQuestionnaireController.getCurrentQuestion()

    fun nextQuestion(): QuestionDefinition? =
        currentQuestionnaireController.getNextQuestion()

    fun previousQuestion() = currentQuestionnaireController.getPreviousQuestion()

    fun getCurrentEvent() = currentQuestionnaireController.getCurrentEvent()

    fun saveQuestionnaireResponses() {

    }

}