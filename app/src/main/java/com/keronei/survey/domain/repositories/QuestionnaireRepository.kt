package com.keronei.survey.domain.repositories

import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.Resource
import com.keronei.survey.domain.models.QuestionnaireDef
import kotlinx.coroutines.flow.Flow

interface QuestionnaireRepository {
    fun getQuestionnaireById(string: String): Flow<QuestionnaireDef>

    suspend fun getQuestionnaires(): Flow<Resource<List<QuestionnaireDef>>>
}