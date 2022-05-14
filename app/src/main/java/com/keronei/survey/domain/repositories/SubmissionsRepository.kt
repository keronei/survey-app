/*
 * Copyright 2022 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.survey.domain.repositories

import com.keronei.survey.core.AnswerData
import com.keronei.survey.data.models.SubmissionStatus
import com.keronei.survey.domain.models.Submission
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface SubmissionsRepository {
    fun getSubmissions(): Flow<List<Submission>>

    suspend fun submitCurrentResponses(): Flow<SubmissionStatus>

    suspend fun saveQuestionnaireResponse(
        questionnaireId: String,
        submissionName: String,
        answers: List<AnswerData>
    )

    suspend fun sendMediaFiles(media: MultipartBody.Part): Flow<Boolean>
}