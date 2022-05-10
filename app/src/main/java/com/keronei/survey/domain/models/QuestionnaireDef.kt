package com.keronei.survey.domain.models

import java.util.*

data class QuestionnaireDef(
    val id: String,
    val numberOfQuestions: Int,
    val language: String,
    val questions: List<QuestionDefinition>,
    val downloadDate: Date
)
