package com.keronei.survey.data.remote

import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.remote.models.QuestionnaireResponse
import com.keronei.survey.domain.models.ServerSubmission
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET

interface QuerySurveysApiService {
    @GET("/v3/d628facc-ec18-431d-a8fc-9c096e00709a")
    suspend fun getQuestionnaires(): QuestionnaireResponse?

    @GET("https://www.pula-advisors.com/")
    suspend fun submitResponses(@Field("submission") submission: ServerSubmission): Response<RequestBody>
}