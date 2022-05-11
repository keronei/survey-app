package com.keronei.survey.data.remote

import com.keronei.survey.core.Resource
import com.keronei.survey.data.remote.models.QuestionnaireResponse
import com.keronei.survey.domain.models.ServerSubmission
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(private val apiService: QuerySurveysApiService) {
    suspend fun getQuestionnaire(): Flow<Resource<QuestionnaireResponse>> = callbackFlow {
        try {
            trySend(Resource.Loading)

            val response = apiService.getQuestionnaires()
            when {
                response != null -> {
                    trySend(Resource.Success(response))
                }
                else -> {
                    trySend(Resource.Failure(Exception("Questionnaire data is null.")))
                }
            }
        } catch (e: IOException) {
            trySend(Resource.Failure(e))
            Timber.e(e)
        } catch (e: Exception) {
            Timber.e(e)
            trySend(Resource.Failure(e))
        }

        awaitClose { close() }
    }

    suspend fun sendSubmissions(submission: ServerSubmission): Flow<Boolean> = callbackFlow {
        try {
            val result = apiService.submitResponses(submission)
            trySend(result.isSuccessful)
        } catch (exception: Exception) {
            exception.printStackTrace()
            trySend(false)
        }
    }
}