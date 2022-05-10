package com.keronei.survey.presentation.models

data class SubmissionPresentation(
    val id: String,
    val submissionDate: String,
    val submissionName: String,
    val questionnaireName: String,
    val synced: Boolean
)
