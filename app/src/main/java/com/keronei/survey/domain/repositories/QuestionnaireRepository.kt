package com.keronei.survey.domain.repositories

import com.keronei.survey.domain.models.QuestionnaireDef

interface QuestionnaireRepository {
    fun getQuestionnaireById(string: String): QuestionnaireDef

    fun getQuestionnaires(): List<QuestionnaireDef>
}