package com.keronei.survey.core

import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef

object QuestionnaireController {
    const val ANSWER_OK = 0

    const val EMPTY_ANSWER = 1

    const val EVENT_BEGINNING_QUESTIONNAIRE = 2

    const val EVENT_END_QUESTIONNAIRE = 3

    const val EVENT_QUESTION = 4

    fun setupController(questionnaireDef: QuestionnaireDef) {

    }

    fun getNextQuestion(): QuestionDefinition {

    }

    fun getPreviousQuestion(): QuestionDefinition {

    }

    fun saveAnswer(answerData: AnswerData, questionDefinition: QuestionDefinition) {

    }


}