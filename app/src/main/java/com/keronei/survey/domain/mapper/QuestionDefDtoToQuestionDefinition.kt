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