package com.keronei.survey.domain.mapper

import com.keronei.survey.data.models.OptionDTO
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.remote.models.ChoiceOptionResponse
import com.keronei.survey.data.remote.models.QuestionDefinitionResponse
import com.keronei.survey.data.remote.models.QuestionnaireResponse
import java.util.*

class QuestionnaireResponseToQuestionnaireDtoMapper :
    Mapper<QuestionnaireResponse, QuestionnaireDefDTO>() {
    override fun map(input: QuestionnaireResponse): QuestionnaireDefDTO {
        return QuestionnaireDefDTO(
            input.id, input.strings.keys.first(), input.questions.map { response ->

                QuestionDefDTO(
                    response.id, response.questionType, response.answerType,
                    getQuestionText(input, response),
                    response.options.map { option ->
                        OptionDTO(
                            option.value, getOptionText(input, option)
                        )

                    },

                    response.next
                )
            },
            input.start_question_id,
            Calendar.getInstance().timeInMillis
        )
    }

    private fun getQuestionText(
        input: QuestionnaireResponse,
        response: QuestionDefinitionResponse
    ): String {
        return input.strings[input.strings.keys.first()]?.get(response.questionText)
            ?: "Unknown question text"
    }

    private fun getOptionText(input: QuestionnaireResponse, option: ChoiceOptionResponse): String {
        return input.strings[input.strings.keys.first()]?.get(option.displayText)
            ?: "Unknown Option text"
    }
}