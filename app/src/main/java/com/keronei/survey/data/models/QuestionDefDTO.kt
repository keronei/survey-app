package com.keronei.survey.data.models

data class QuestionDefDTO(
    val id: String,
    val questionType: String,
    val answerType: String,
    val questionText: String,
    val options: List<OptionDTO>,
    val next: String?
)
