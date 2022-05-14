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
package com.keronei.survey

import com.keronei.survey.core.QuestionnaireController
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.QuestionnaireSubDTO
import com.keronei.survey.domain.mapper.QuestionnaireDefDtoToQuestionnaireDefMapper
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class TestController {

    private val controller = QuestionnaireController

    private val testOneId = "test_1"
    private val testTwoId = "test_2"

    private val question1 = QuestionDefDTO(
        testOneId,
        "Where is one number?",
        "FREE_TEXT",
        "who owns the farm test 1",
        listOf(),
        testTwoId
    )
    private val question2 = QuestionDefDTO(
        testTwoId,
        "Which county are you from?",
        "MULTILINE_FREE_TEXT",
        "what source of water does the farmer use test 2",
        listOf(),
        null
    )

    private val questionnaire = QuestionnaireDefDTO(
        "test_qs",
        "en",
        listOf(question1, question2),
        testOneId,
        Calendar.getInstance().timeInMillis
    )

    @Before
    fun setup() {
        controller.setupController(
            QuestionnaireDefDtoToQuestionnaireDefMapper().map(
                QuestionnaireSubDTO(
                    questionnaire.id,
                    questionnaire.language,
                    questionnaire.questions,
                    questionnaire.startQuestionId,
                    questionnaire.downloadDate,
                    0
                )
            )
        )
    }

    @Test
    fun pushing_two_questions_returns_one_with_next_question_first() {
        val firstQuestion = controller.getCurrentQuestion()
        assertThat("Current question should be first", firstQuestion!!.id == question1.id)
    }

    @Test
    fun next_question_matches_test_2() {
        controller.getNextQuestion() // starts at first question
        val nextQuestion = controller.getNextQuestion() // qsn 2

        assertThat(
            "Next should return test_2 because image capture is added by the controller as last question",
            nextQuestion!!.id == "test_2"
        )
    }

    @Test
    fun next_question_after_2_is_one_without_next() {
        controller.getNextQuestion() // returns qsn_1
        controller.getNextQuestion() // returns qsn2
        val nextQuestion = controller.getNextQuestion() // returns img_capture

        assert(
            nextQuestion!!.nextQuestion == null,
        ) { "Next(should be 3rd and last - image capture) does not have next question, because is the last" }
    }

    @Test
    fun back_navigation_before_next_is_null() {
        val previous = controller.getPreviousQuestion()

        assert(previous == null) {
            "On start, there is no previous question"
        }
    }

    @Test
    fun `Going forward three steps and back once stops at qsn 2`() {
        controller.getNextQuestion()
        controller.getNextQuestion()
        controller.getNextQuestion()
        val previous = controller.getPreviousQuestion()

        assert(previous!!.id == testTwoId) { "3 - 1 should be 2." }
    }

    @Test
    fun `Going back a step then 2 steps ahead still returns the second question`() {
        controller.getPreviousQuestion()
        controller.getNextQuestion()
        val second = controller.getNextQuestion()
        assert(second!!.id == testTwoId) { "-0 + 2 = 2" }
    }

    @Test
    fun `Navigating back and forth doesn't mix up`() {
        controller.getNextQuestion()
        controller.getPreviousQuestion() // did not make any change on index, therefore the next next builds on first one
        controller.getNextQuestion()
        controller.getNextQuestion()
        val qsn = controller.getPreviousQuestion()

        assert(qsn!!.id == testTwoId) { "+1 - 0 + 2 - 1 = 2" }
    }
}