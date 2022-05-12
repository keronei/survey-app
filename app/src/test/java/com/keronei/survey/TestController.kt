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

    private val question1 = QuestionDefDTO(
        "test_1",
        "text",
        "text",
        "who owns the farm test 1",
        listOf(),
        "test_2"
    )
    private val question2 = QuestionDefDTO(
        "test_2",
        "text",
        "text",
        "what source of water does the farmer use test 2",
        listOf(),
        null
    )

    private val questionnaire = QuestionnaireDefDTO(
        "test_qs",
        "en",
        listOf(question1, question2),
        "test_1",
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
        controller.getNextQuestion()// starts at first question
        val nextQuestion = controller.getNextQuestion()// qsn 2

        assertThat(
            "Next should return test_2 because image capture is added by the controller as last question",
            nextQuestion!!.id == "test_2"
        )
    }

    @Test
    fun next_question_after_2_is_one_without_next() {
        controller.getNextQuestion() // returns qsn_1
        controller.getNextQuestion() // returns qsn2
        val nextQuestion = controller.getNextQuestion()// returns img_capture


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

        assert(previous!!.id == "test_2") { "3 - 1 should be 2." }
    }

    @Test
    fun `Going back a step then 2 steps ahead still returns the second question`() {
        controller.getPreviousQuestion()
        controller.getNextQuestion()
        val second = controller.getNextQuestion()
        assert(second!!.id == "test_2"){"-0 + 2 = 2"}
    }

    @Test
    fun `Navigating back and forth doesn't mix up`(){
        controller.getNextQuestion()
        controller.getPreviousQuestion()// did not make any change on index, therefore the next next builds on first one
        controller.getNextQuestion()
        controller.getNextQuestion()
        val qsn = controller.getPreviousQuestion()

        println("Found: $qsn")

        assert(qsn!!.id == "test_2"){"+1 - 0 + 2 - 1 = 2"}
    }
}