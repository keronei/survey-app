package com.keronei.survey.domain.repositories

import com.keronei.survey.domain.models.Submission
import kotlinx.coroutines.flow.Flow

interface SubmissionsRepository {
    fun getSubmissions(): Flow<List<Submission>>

    fun submitCurrentResponses()
}