package com.keronei.survey.domain.repositories

import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.Submission
import kotlinx.coroutines.flow.Flow

interface SubmissionsRepository {
    fun getSubmissions(): Flow<List<Submission>>

    suspend fun submitCurrentResponses()

    fun saveQuestionnaireResponse(
        questionnaireId: String,
        submissionName: String,
        answers: List<AnswerData>
    )
}