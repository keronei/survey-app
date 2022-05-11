package com.keronei.survey.data.remote.models

import com.google.gson.annotations.SerializedName
import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.AnswerType
import com.keronei.survey.core.QuestionType

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
