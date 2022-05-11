package com.keronei.survey.data.remote.models

data class QuestionnaireResponse(
    val id: String,
    val start_question_id: String,
    val strings: Map<String, Map<String, String>>,
    val questions: List<QuestionDefinitionResponse>,
)
