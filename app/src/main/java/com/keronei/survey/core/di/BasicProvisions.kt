package com.keronei.survey.core.di

import android.content.Context
import com.keronei.survey.data.remote.QuerySurveysApiService
import com.keronei.survey.helpers.ConnectivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
        if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE)

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
    fun providesRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://run.mocky.io")
        .addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): QuerySurveysApiService =
        retrofit.create(QuerySurveysApiService::class.java)
}