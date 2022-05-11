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
package com.keronei.survey.data.remote.models

import com.google.gson.annotations.SerializedName

data class QuestionDefinitionResponse(
    val id: String,
    @SerializedName("question_type")
    val questionType: String,
    @SerializedName("answer_type")
    val answerType: String,
    @SerializedName("question_text")
    val questionText: String,
    val options: List<ChoiceOptionResponse>,
    val next: String?
)