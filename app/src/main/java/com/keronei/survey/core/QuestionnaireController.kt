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
        attachPreceding(questionnaireDef.questions, questions)
        currentQuestion = questions.eventNodeAt(0)
    }

    private fun attachPreceding(
        list: List<QuestionDefinition>,
        linkedList: LinkedList<QuestionDefinition>
    ): LinkedList<QuestionDefinition> {
        var parent = ""
        var toRemove: QuestionDefinition? = null

        if (parent == "") {
            // last-born node
            val item = list.firstOrNull { node -> node.nextQuestion == null }
            if (item != null) {
                parent = item.id
                toRemove = item
            }
        } else {
            // has parent previous
            toRemove = list.firstOrNull { item -> item.id == parent }
            parent = toRemove?.nextQuestion ?: ""
        }

        if (list.isEmpty()) {
            return linkedList
        }

        val nextList = mutableListOf<QuestionDefinition>()

        nextList.addAll(list)

        toRemove?.let {
            questions.push(it)
            nextList.remove(toRemove)
        }

        return attachPreceding(nextList, linkedList)
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
        if (currentIndex == 0 && currentQuestion == null && !questions.isEmpty()) {
            return EVENT_BEGINNING_QUESTIONNAIRE
        } else if (currentIndex > 0 && currentQuestion != null) {
            return EVENT_QUESTION
        } else if (currentIndex > 0 && currentQuestion == null) {
            return EVENT_END_QUESTIONNAIRE
        }
        return UNKNOWN_EVENT
    }

    fun saveQuestionResponse(questionDefinition: QuestionDefinition) {
        responses[questionDefinition.id] = questionDefinition.answerData!!
    }

    fun getQuestionnaireResponses() = responses.values.toList()
}