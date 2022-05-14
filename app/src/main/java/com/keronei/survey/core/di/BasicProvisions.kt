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
package com.keronei.survey.core.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.keronei.survey.BuildConfig
import com.keronei.survey.data.remote.QuerySurveysApiService
import com.keronei.survey.helpers.ConnectivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BasicProvisions {
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityProvider =
        ConnectivityProvider(
            context
        )

    @Singleton
    @Provides
    fun interceptWithLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    )

    @Singleton
    @Provides
    fun providesOkHttp3Instance(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                loggingInterceptor
            ).build()

    @Singleton
    @Provides
    fun providesRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        // set lenient to allow loading PULA site without parsing the json.
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            ).build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): QuerySurveysApiService =
        retrofit.create(QuerySurveysApiService::class.java)

    @Provides
    @Singleton
    fun providesPreferenceInstance(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

}