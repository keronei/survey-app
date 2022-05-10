package com.keronei.survey.domain.repositories

import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionnaireDef
import kotlinx.coroutines.flow.Flow

interface QuestionnaireRepository {
    fun getQuestionnaireById(string: String): Flow<QuestionnaireDef>

    fun getQuestionnaires(): Flow<List<QuestionnaireDef>>

    fun saveQuestionnaireResponse(questionnaireId: String, answers: List<AnswerData>)
}