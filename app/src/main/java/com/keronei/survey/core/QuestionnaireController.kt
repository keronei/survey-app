package com.keronei.survey.core

import com.keronei.survey.core.events.EventNode
import com.keronei.survey.core.events.LinkedList
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef

object QuestionnaireController {
    const val ANSWER_OK = 0

    const val EMPTY_ANSWER = 1

    const val EVENT_BEGINNING_QUESTIONNAIRE = 2

    const val EVENT_END_QUESTIONNAIRE = 3

    const val EVENT_QUESTION = 4

    const val UNKNOWN_EVENT = 5

    private val questions = LinkedList<QuestionDefinition>()
    private var currentIndex = 0
    private var currentQuestion: EventNode<QuestionDefinition>? = null
    private val responses = mutableMapOf<String, AnswerData>()

    fun setupController(questionnaireDef: QuestionnaireDef) {
        for (question in questionnaireDef.questions) {
            questions.append(question)
        }
        currentQuestion = questions.eventNodeAt(0)
    }

    fun getCurrentQuestion() = currentQuestion?.value

    fun getNextQuestion(): QuestionDefinition? {
        currentQuestion = if (currentIndex == 0) {
            currentIndex++
            questions.eventNodeAt(0)
        } else {
            currentIndex++
            currentQuestion?.next
        }
        return currentQuestion?.value
    }

    fun getPreviousQuestion(): QuestionDefinition? {
        if (currentIndex == 0) {
            return null
        }
        currentIndex--
        currentQuestion = questions.eventNodeAt(currentIndex)

        return currentQuestion?.value
    }

    fun getCurrentEvent(): Int {
        if (currentIndex == 0 && currentQuestion == null && !questions.isEmpty()){
            return EVENT_BEGINNING_QUESTIONNAIRE
        } else if(currentIndex > 0 && currentQuestion != null){
            return EVENT_QUESTION
        }else if(currentIndex > 0 && currentQuestion == null){
            return EVENT_END_QUESTIONNAIRE
        }
        return UNKNOWN_EVENT
    }

    fun saveQuestionResponse(questionDefinition: QuestionDefinition) {
        responses[questionDefinition.id] = questionDefinition.answerData!!
    }

    fun getQuestionnaireResponses() = responses.values.toList()

}