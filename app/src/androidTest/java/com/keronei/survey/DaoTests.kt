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
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DaoTests {

    private lateinit var questionnaireDao: QuestionnaireDao
    private lateinit var submissionsDao: SubmissionsDao
    private lateinit var db: SurveyAppDatabase
    private val calendar: Long = Calendar.getInstance().timeInMillis

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
        calendar
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

    @Ignore("Likely testing the library")
    @Test
    fun deleting_questionnaire_with_submission_fails(): Unit = runBlocking {
        createQuestionnaireEntry()
        createSubmission()

        val deletedCount = questionnaireDao.deleteQuestionnaire(questionnaire)

        assertThat("No questionnaire was deleted", deletedCount == 0)
    }

    private fun createQuestionnaireEntry() = runBlocking {
        questionnaireDao.addQuestionnaire(
            questionnaire
        )
    }


    @Ignore("Likely testing the library")
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

    @Test
    fun adding_questionnaire_with_given_add_time_returns_correct_time() = runBlocking {
        createQuestionnaireEntry()
        val questionnaireWithCount = questionnaireDao.getAllQuestionnaires().first()
        println("Returned value:${questionnaireWithCount.first().downloadDate} expected: $calendar")

        assertThat("Creation date should be returned as added.", questionnaireWithCount.first().downloadDate == calendar)
    }

    private fun createSubmission() = runBlocking{
        submissionsDao.addSubmission(
            SubmissionsDTO(
                0,
                calendar,
                "test_qs",
                "test submission",
                "{question:answer}",
                false
            )
        )
    }

}