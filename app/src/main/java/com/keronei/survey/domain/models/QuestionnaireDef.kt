package com.keronei.survey.domain.models

data class QuestionnaireDef(
    val id: String,
    val numberOfQuestions: Int,
    val language: String,
    val questions: List<QuestionDefinition>
)
