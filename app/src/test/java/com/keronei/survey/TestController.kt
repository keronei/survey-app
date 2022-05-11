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
        val nextQuestion = controller.getNextQuestion()

        println(nextQuestion)

        assertThat(
            "Next should return test_2 because image capture is added by the controller as last question",
            nextQuestion!!.id == "test_2"
        )
    }

    @Test
    fun next_question_after_2_is_one_without_next() {
        val first = controller.getNextQuestion() // returns qsn_1
        println(first)
        val second = controller.getNextQuestion() // returns qsn2
        println(second)
        val nextQuestion = controller.getNextQuestion()// returns img_capture

        println("Expected img_capture"+nextQuestion)
        assert(
            nextQuestion!!.nextQuestion == null,
        ) { "Next(should be 3rd and last - image capture) does not have next question, because is the last" }
    }
}