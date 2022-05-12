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
package com.keronei.survey.data

import com.keronei.survey.core.Resource
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.remote.RemoteDataSource
import com.keronei.survey.domain.mapper.QuestionnaireDefDtoToQuestionnaireDefMapper
import com.keronei.survey.domain.mapper.QuestionnaireResponseToQuestionnaireDtoMapper
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class QuestionnairesRepoImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : QuestionnaireRepository {
    private val mapper = QuestionnaireDefDtoToQuestionnaireDefMapper()

    override fun getQuestionnaireById(string: String): Flow<QuestionnaireDef> {

        val result = localDataSource.getQuestionnaireById(string)

        return result.map { item ->
            mapper.map(item)
        }
    }

    override suspend fun getQuestionnaires(): Flow<Resource<List<QuestionnaireDef>>> =
        callbackFlow {
            trySend(Resource.Loading)

            val definitions = localDataSource.getQuestionnaires()

            val local = definitions.map { list ->
                mapper.mapList(list)
            }

            val availableLocally = local.first()

            if (availableLocally.isEmpty()) {
                remoteDataSource.getQuestionnaire().collect { remoteResult ->
                    when (remoteResult) {
                        is Resource.Failure -> {
                            trySend(Resource.Failure(remoteResult.exception))
                        }
                        Resource.Loading -> {
                            trySend(Resource.Loading)
                        }
                        is Resource.Success -> {
                            val data = remoteResult.data

                            val forSaving =
                                QuestionnaireResponseToQuestionnaireDtoMapper().map(data)

                            localDataSource.addQuestionnaire(
                                forSaving
                            )

                            val def = localDataSource.getQuestionnaires()

                            val updatedLocal = def.map { list ->
                                mapper.mapList(list)
                            }

                            // offer from local
                            trySend(Resource.Success(updatedLocal.first()))
                        }
                    }
                }
            } else {
                trySend(Resource.Success(availableLocally))
            }


            awaitClose { close() }
        }
}