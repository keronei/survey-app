package com.keronei.survey

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.local.database.SurveyAppDatabase
import com.keronei.survey.data.models.QuestionDefDTO
import com.keronei.survey.data.models.QuestionnaireDefDTO
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
    fun `create questionnaire adds a questionnaire to db`(): Unit = runBlocking {
        val questionnaire = QuestionnaireDefDTO(
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

        questionnaireDao.addQuestionnaire(
            questionnaire.id,
            questionnaire.language,
            questionnaire.questions,
            questionnaire.startQuestionId
        )

       val surveys = questionnaireDao.getAllQuestionnaires()

        assertThat("An item has already been inserted.", surveys.first().size == 1)
    }


}