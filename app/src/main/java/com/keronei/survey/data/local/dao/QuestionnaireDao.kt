package com.keronei.survey.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDao {
    @Query("INSERT INTO QuestionnaireDefDTO(id, language, questions, startQuestionId) values(:id, :language, :questions, :startQuestionId)")
    fun addQuestionnaire(
        id: String,
        language: String,
        questions: List<QuestionDefDTO>,
        startQuestionId: String
    ): Long

    @Query("SELECT * FROM QuestionnaireDefDTO")
    fun getAllQuestionnaires(): Flow<QuestionnaireDefDTO>

    @Query("SELECT * FROM QuestionnaireDefDTO WHERE id = :id")
    fun getQuestionnaireById(id: String): Flow<QuestionnaireDefDTO>

    @Delete
    fun deleteQuestionnaire(questionnaireDefDTO: QuestionnaireDefDTO): Int
}