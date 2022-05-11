package com.keronei.survey.data

import com.keronei.survey.core.Resource
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.remote.NetworkDataSource
import com.keronei.survey.domain.mapper.QuestionnaireDefDtoToQuestionnaireDefMapper
import com.keronei.survey.domain.mapper.QuestionnaireResponseToQuestionnaireDtoMapper
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
class QuestionnairesRepoImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource
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

            local.collect { qns ->
                if (qns.isEmpty()) {
                    networkDataSource.getQuestionnaire().collect { remoteResult ->
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
                                    forSaving.id,
                                    forSaving.language,
                                    forSaving.questions,
                                    forSaving.startQuestionId
                                )
                            }
                        }
                    }


                } else {
                    trySend(Resource.Success(qns))
                }

            }
            awaitClose { close() }
        }

}