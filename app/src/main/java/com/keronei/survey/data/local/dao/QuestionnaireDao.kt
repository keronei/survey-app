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
package com.keronei.survey.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.QuestionnaireSubDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionnaireDao {
    @Query("INSERT INTO QuestionnaireDefDTO(id, language, questions, startQuestionId) values(:id, :language, :questions, :startQuestionId)")
    suspend fun addQuestionnaire(
        id: String,
        language: String,
        questions: List<QuestionDefDTO>,
        startQuestionId: String
    ): Long

    @Query(
        "SELECT * , (SELECT COUNT(*) FROM SubmissionsDTO" +
            " WHERE SubmissionsDTO.questionnaireId = QuestionnaireDefDTO.id)" +
            " AS submissionsCount FROM QuestionnaireDefDTO"
    )
    fun getAllQuestionnaires(): Flow<List<QuestionnaireSubDTO>>

    @Query("SELECT *, (SELECT COUNT(*) FROM SubmissionsDTO"+
            " WHERE SubmissionsDTO.questionnaireId = QuestionnaireDefDTO.id)" +
            " AS submissionsCount  FROM QuestionnaireDefDTO WHERE id = :id")
    fun getQuestionnaireById(id: String): Flow<QuestionnaireSubDTO>

    @Delete
    suspend fun deleteQuestionnaire(questionnaireDefDTO: QuestionnaireDefDTO): Int
}