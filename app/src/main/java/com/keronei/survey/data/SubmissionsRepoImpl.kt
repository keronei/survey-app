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
package com.keronei.survey.data

import com.google.gson.Gson
import com.keronei.survey.core.AnswerData
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.SubmissionsDTOUpdate
import com.keronei.survey.data.remote.RemoteDataSource
import com.keronei.survey.domain.mapper.SubmissionsDtoToSubmissionMapper
import com.keronei.survey.domain.models.ServerSubmission
import com.keronei.survey.domain.models.Submission
import com.keronei.survey.domain.repositories.SubmissionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SubmissionsRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : SubmissionsRepository {
    override fun getSubmissions(): Flow<List<Submission>> {
        val submissions = localDataSource.getSubmissions()
        return submissions.map { dto ->
            SubmissionsDtoToSubmissionMapper().mapList(dto)
        }
    }

    override suspend fun submitCurrentResponses() {
        val submissions = localDataSource.getSubmissions()

        val objectForDispatch = submissions.map { submissionsItem ->
            ServerSubmission(
                submissionsItem.first().questionnaireId,
                Gson().toJson(submissionsItem.map { item -> item.submissionAsJson })
            )
        }
        val ids = submissions.map { updates ->
            updates.map { entry ->
                SubmissionsDTOUpdate(entry.id, true)
            }
        }

        val item = objectForDispatch.first()
        val result = remoteDataSource.sendSubmissions(item)

        // mark sync status for dispatched responses
        val successStatus = result.first()
        if (successStatus) {
            val toMark = ids.first()
            toMark.forEach {
                localDataSource.markAsSynced(it)
            }
        }
    }

    override suspend fun saveQuestionnaireResponse(
        questionnaireId: String,
        submissionName: String,
        answers: List<AnswerData>
    ) {
        val calendar = Calendar.getInstance()
        localDataSource.createSubmission(
            SubmissionsDTO(
                0,
                calendar.timeInMillis,
                questionnaireId,
                submissionName,
                Gson().toJson(answers),
                false
            )
        )
    }
}