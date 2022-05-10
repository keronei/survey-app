package com.keronei.survey.data

import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.QuestionType
import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.remote.NetworkDataSource
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.domain.repositories.QuestionnaireRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestionnairesRepoImpl @Inject constructor(
    val localDataSource: LocalDataSource,
    val networkDataSource: NetworkDataSource
) : QuestionnaireRepository {
    override fun getQuestionnaireById(string: String): Flow<QuestionnaireDef> {

        val result = localDataSource.getQuestionnaireById(string)

        result.map { item ->
            QuestionnaireDef(
                item.id,
                item.questions.size,
                item.language,
                item.questions.map { qns -> QuestionDefinition(qns.id, qns.) })

        }
    }

    private fun getQuestionType(value: String): QuestionType {
        return when (value) {
            "FREE_TEXT" -> QuestionType.FREE_TEXT
            "SELECT_ONE" -> QuestionType.SELECT_ONE
            "TYPE_VALUE" -> QuestionType.TYPE_VALUE
            else -> QuestionType.FREE_TEXT
        }
    }

    override fun getQuestionnaires(): Flow<List<QuestionnaireDef>> {
        TODO("Not yet implemented")
    }

    override fun saveQuestionnaireResponse(questionnaireId: String, answers: List<AnswerData>) {
        TODO("Not yet implemented")
    }
}