package com.keronei.survey.domain.models

data class Submission(
    val id: String,
    val submissionDate: String,
    val submissionName: String,
    val questionnaireName: String,
    val synced: Boolean
)
