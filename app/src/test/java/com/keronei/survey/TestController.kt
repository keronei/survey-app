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
        "who owns the farm",
        listOf(),
        "test_2"
    )
    private val question2 = QuestionDefDTO(
        "test_2",
        "text",
        "text",
        "who owns the farm",
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

        assertThat("With next question should be first", firstQuestion!!.id == question1.id)

    }

    @Test
    fun next_question_is_one_without_next() {
        val nextQuestion = controller.getNextQuestion()

        assertThat(
            "Next does not have next question, because is the last",
            nextQuestion!!.nextQuestion == null
        )
    }
}