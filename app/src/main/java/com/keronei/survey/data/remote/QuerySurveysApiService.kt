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
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Part

interface QuerySurveysApiService {
    @GET("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getQuestionnaires(): QuestionnaireResponse?

    @GET("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun submitResponses(): Response<Any>

    @Headers("Cache-Control: no-cache")
    @POST("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun sendImage( @Part photo: MultipartBody.Part): Response<Any>
}