package com.keronei.survey.domain.models

data class Submission(
    val id: Int,
    val submissionDate: Long,
    val submissionName: String,
    val questionnaireId: String,
    val synced: Boolean
)
