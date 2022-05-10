package com.keronei.survey.core.di

import com.keronei.survey.data.QuestionnairesRepoImpl
import com.keronei.survey.data.SubmissionsRepoImpl
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.remote.NetworkDataSource
import com.keronei.survey.data.remote.QuerySurveysApiService
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import com.keronei.survey.domain.repositories.SubmissionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun providesLocalDataSource(
        questionnaireDao: QuestionnaireDao,
        submissionsDao: SubmissionsDao
    ): LocalDataSource = LocalDataSource(submissionsDao, questionnaireDao)

    @Singleton
    @Provides
    fun providesNetworkDataSource(apiService: QuerySurveysApiService): NetworkDataSource =
        NetworkDataSource(apiService)

    @Singleton
    @Provides
    fun providesQuestionnairesRepo(
        localDataSource: LocalDataSource,
        networkDataSource: NetworkDataSource
    ): QuestionnaireRepository = QuestionnairesRepoImpl(localDataSource, networkDataSource)

    @Singleton
    @Provides
    fun providesSubmissionsRepo(
        localDataSource: LocalDataSource,
        networkDataSource: NetworkDataSource
    ): SubmissionsRepository = SubmissionsRepoImpl(networkDataSource, localDataSource)
}