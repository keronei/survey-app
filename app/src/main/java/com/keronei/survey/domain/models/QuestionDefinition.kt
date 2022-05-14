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
package com.keronei.survey.domain.models

import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.AnswerType
import com.keronei.survey.core.QuestionType

data class QuestionDefinition(
    val id: String,
    val questionType: QuestionType,
    val answerType: AnswerType,
    val questionText: String,
    val options: List<ChoiceOption>,
    val nextQuestion: String?,
    var answerData: AnswerData?
)