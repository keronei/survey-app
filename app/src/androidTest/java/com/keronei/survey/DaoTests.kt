package com.keronei.survey

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.local.database.SurveyAppDatabase
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.SubmissionsDTO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DaoTests {

    private lateinit var questionnaireDao: QuestionnaireDao
    private lateinit var submissionsDao: SubmissionsDao
    private lateinit var db: SurveyAppDatabase
    private val calendar = Calendar.getInstance()

    private val questionnaire = QuestionnaireDefDTO(
        "test_qs",
        "en",
        listOf(
            QuestionDefDTO(
                "test_1",
                "text",
                "text",
                "who owns the farm",
                listOf(),
                null
            )
        ),
        "test_1",
        Calendar.getInstance().timeInMillis
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, SurveyAppDatabase::class.java
        ).build()
        questionnaireDao = db.surveyDao()
        submissionsDao = db.submissionDao()
    }

    @Test
    fun create_questionnaire_adds_a_questionnaire_to_db(): Unit = runBlocking {

        createQuestionnaireEntry()

        val surveys = questionnaireDao.getAllQuestionnaires()

        assertThat("An item has already been inserted.", surveys.first().size == 1)
    }

    @Test
    fun deleting_questionnaire_with_submission_fails(): Unit = runBlocking {
        createQuestionnaireEntry()
        createSubmission()

        val deletedCount = questionnaireDao.deleteQuestionnaire(questionnaire)

        assertThat("No questionnaire was deleted", deletedCount == 0)
    }

    private fun createQuestionnaireEntry() =
        questionnaireDao.addQuestionnaire(
            questionnaire.id,
            questionnaire.language,
            questionnaire.questions,
            questionnaire.startQuestionId
        )


    @Test
    fun creating_submission_without_questionnaire_fails(): Unit = runBlocking {
        createSubmission()

        val submissions = submissionsDao.getSubmissions()

        assertThat("No Submission should exist", submissions.first().isEmpty())
    }

    @Test
    fun query_survey_with_submission_returns_submission_count(): Unit = runBlocking {
        createQuestionnaireEntry()

        createSubmission()

        val questionnaireWithCount = questionnaireDao.getAllQuestionnaires().first()

        assertThat(
            "A questionnaire with submission should have count > 0",
            questionnaireWithCount.first().submissionsCount > 0
        )
    }

    private fun createSubmission() {
        submissionsDao.addSubmission(
            SubmissionsDTO(
                0,
                calendar.timeInMillis,
                "test_qs",
                "test submission",
                "{question:answer}",
                false
            )
        )
    }

}