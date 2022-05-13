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

import com.keronei.survey.data.remote.models.QuestionnaireResponse
import com.keronei.survey.domain.models.ServerSubmission
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface QuerySurveysApiService {
    @GET("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getQuestionnaires(): QuestionnaireResponse?

    @GET("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun submitResponses(
    ): Response<Any>
}