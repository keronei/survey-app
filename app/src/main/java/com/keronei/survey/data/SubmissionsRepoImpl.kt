package com.keronei.survey.data

import com.google.gson.Gson
import com.keronei.survey.core.AnswerData
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.SubmissionsDTOUpdate
import com.keronei.survey.data.remote.NetworkDataSource
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
    private val remoteDataSource: NetworkDataSource,
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

    override fun saveQuestionnaireResponse(
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