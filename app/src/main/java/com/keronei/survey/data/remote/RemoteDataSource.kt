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
package com.keronei.survey.data.remote

import com.keronei.survey.core.Resource
import com.keronei.survey.data.remote.models.QuestionnaireResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RemoteDataSource @Inject constructor(private val apiService: QuerySurveysApiService) {
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
        } catch (noNetwork: java.net.UnknownHostException) {
            trySend(Resource.Failure(Exception("Check your internet connection and try again.")))
        } catch (e: IOException) {
            trySend(Resource.Failure(e))
            Timber.e(e)
        } catch (e: Exception) {
            Timber.e(e)
            trySend(Resource.Failure(e))
        }

        awaitClose { close() }
    }

    suspend fun sendSubmissions(surveyId: String, submission: String): Flow<Boolean> =
        callbackFlow {
            /**
             *  dropping the submission data here.
             *  You should use @link{@FormUlrEncoded } and @Field parameters to attach the survey ID and actual responses.
             */

            try {
                val result = apiService.submitResponses()
                trySend(result.isSuccessful)
            } catch (exception: Exception) {
                exception.printStackTrace()
                trySend(false)
            }

            awaitClose { cancel() }
        }

    suspend fun sendMediaFiles(media: MultipartBody.Part) = callbackFlow {
        try {
            val sending = apiService.sendImage(media)
            trySend(sending.isSuccessful)
        } catch (exception: Exception) {
            exception.printStackTrace()
            trySend(false)
        }
    }
}