package com.keronei.survey.data.local

import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.SubmissionsDTOUpdate
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    val submissionsDao: SubmissionsDao,
    val questionnaireDao: QuestionnaireDao
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