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
import com.keronei.survey.domain.models.ChoiceOption
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef

object QuestionnaireController {
    const val ANSWER_OK = 0

    const val EMPTY_ANSWER = 1

    const val EVENT_BEGINNING_QUESTIONNAIRE = 2

    const val EVENT_END_QUESTIONNAIRE = 3

    const val EVENT_QUESTION = 4

    const val UNKNOWN_EVENT = 5

    private var questions = LinkedList<QuestionDefinition>()
    private var currentIndex = 0
    private var currentQuestion: EventNode<QuestionDefinition>? = null
    private val responses = mutableMapOf<String, AnswerData>()

    fun setupController(questionnaireDef: QuestionnaireDef) {
        //clear previous config
        questions = LinkedList()
        responses.clear()
        currentIndex = -1
        currentQuestion = null
        // hack to add image capture in the end
        // section
        val mutableListOfQuestion = mutableListOf<QuestionDefinition>()

        mutableListOfQuestion.addAll(questionnaireDef.questions)

        val question =
            questionnaireDef.questions.first { questionDefinition -> questionDefinition.nextQuestion == null }

        val predefinedLastQuestion = QuestionDefinition(
            question.id,
            question.questionType,
            question.answerType,
            question.questionText,
            question.options,
            "image_capture",
            null
        )

        mutableListOfQuestion.remove(question)

        val imageCaptureQuestion = QuestionDefinition(
            "image_capture",
            QuestionType.IMAGE_CAPTURE,
            AnswerType.IMAGE_PATH,
            "Take a picture.",
            emptyList(),
            null,
            null
        )

        val additions = listOf(predefinedLastQuestion, imageCaptureQuestion)

        mutableListOfQuestion.addAll(additions)
        // end section

        attachPreceding(mutableListOfQuestion, questions, questionnaireDef.startQuestion)
        currentQuestion = questions.eventNodeAt(0)
    }

    private fun attachPreceding(
        list: MutableList<QuestionDefinition>,
        linkedList: LinkedList<QuestionDefinition>,
        next: String?
    ): LinkedList<QuestionDefinition> {

        if (next == null) {
            // last-born node
            val item = list.firstOrNull { node -> node.nextQuestion == null }
            if (item != null) {
                if (list.size == 1) {
                    questions.append(item)
                    return linkedList
                }
            }
        } else {
            // previous id points to next of current qsn
            val item = list.firstOrNull { item -> item.id == next }// first parent

            if (item != null) {
                return if (list.size == 1) {
                    questions.append(list.first())
                    linkedList
                } else {
                    list.remove(item)

                    questions.append(item)

                    attachPreceding(list, linkedList, item.nextQuestion)
                }

            }

        }

        return linkedList
    }

    fun getCurrentQuestion() = currentQuestion?.value

    fun getNextQuestion(): QuestionDefinition? {
        if (currentIndex == -1) {// first time call
            currentIndex += 1
            currentQuestion = questions.eventNodeAt(0)
        } else {
            currentQuestion = currentQuestion?.next
            currentIndex += 1
        }
        return currentQuestion?.value
    }

    fun getPreviousQuestion(): QuestionDefinition? {
        if (currentIndex < 1) {
            return null
        }

        currentIndex -= 1
        currentQuestion = questions.eventNodeAt(currentIndex)
        return currentQuestion?.value
    }

    fun getCurrentEvent(): Int {
        if (currentIndex < 1 && currentQuestion == null && !questions.isEmpty()) {
            return EVENT_BEGINNING_QUESTIONNAIRE
        } else if (currentIndex > -1 && currentQuestion != null) {
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