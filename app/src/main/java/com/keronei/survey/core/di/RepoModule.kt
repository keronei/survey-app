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

import com.keronei.survey.data.QuestionnairesRepoImpl
import com.keronei.survey.data.SubmissionsRepoImpl
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.remote.RemoteDataSource
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
    fun providesNetworkDataSource(apiService: QuerySurveysApiService): RemoteDataSource =
        RemoteDataSource(apiService)

    @Singleton
    @Provides
    fun providesQuestionnairesRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): QuestionnaireRepository = QuestionnairesRepoImpl(localDataSource, remoteDataSource)

    @Singleton
    @Provides
    fun providesSubmissionsRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): SubmissionsRepository = SubmissionsRepoImpl(remoteDataSource, localDataSource)
}