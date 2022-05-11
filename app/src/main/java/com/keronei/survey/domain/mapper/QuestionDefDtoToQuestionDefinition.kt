package com.keronei.survey.domain.mapper

import com.keronei.survey.core.AnswerType
import com.keronei.survey.core.QuestionType
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.domain.models.ChoiceOption
import com.keronei.survey.domain.models.QuestionDefinition

class QuestionDefDtoToQuestionDefinition : Mapper<QuestionDefDTO, QuestionDefinition>() {
    override fun map(input: QuestionDefDTO): QuestionDefinition {
        return QuestionDefinition(
            input.id,
            getQuestionType(input.questionType),
            getAnswerType(input.answerType),
            input.questionText,
            input.options.map { optionDTO -> ChoiceOption(optionDTO.value, optionDTO.displayText) },
            input.next,
            null
        )
    }

    private fun getQuestionType(value: String): QuestionType {
        return when (value) {
            "FREE_TEXT" -> QuestionType.FREE_TEXT
            "SELECT_ONE" -> QuestionType.SELECT_ONE
            "TYPE_VALUE" -> QuestionType.TYPE_VALUE
            else -> QuestionType.FREE_TEXT
        }
    }

    private fun getAnswerType(value: String): AnswerType {
        return when (value) {
            "SINGLE_LINE_TEXT" -> AnswerType.SINGLE_LINE_TEXT
            "FLOAT" -> AnswerType.FLOAT
            else -> AnswerType.SINGLE_LINE_TEXT
        }
    }


}