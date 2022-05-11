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
package com.keronei.survey.data.local

import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.SubmissionsDTOUpdate
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val submissionsDao: SubmissionsDao,
    private val questionnaireDao: QuestionnaireDao
) {
    fun getQuestionnaires() = questionnaireDao.getAllQuestionnaires()

    fun getQuestionnaireById(id: String) = questionnaireDao.getQuestionnaireById(id)

    fun addQuestionnaire(
        id: String,
        language: String,
        questions: List<QuestionDefDTO>,
        startQuestion: String
    ) = questionnaireDao.addQuestionnaire(id, language, questions, startQuestion)

    fun deleteQuestionnaire(questionnaireDefDTO: QuestionnaireDefDTO) =
        questionnaireDao.deleteQuestionnaire(questionnaireDefDTO)

    fun getSubmissions() = submissionsDao.getSubmissions()

    fun createSubmission(submissionsDTO: SubmissionsDTO) =
        submissionsDao.addSubmission(submissionsDTO)

    fun markAsSynced(submissionsDTOUpdate: SubmissionsDTOUpdate) =
        submissionsDao.markSubmissionAsSynced(submissionsDTOUpdate)
}