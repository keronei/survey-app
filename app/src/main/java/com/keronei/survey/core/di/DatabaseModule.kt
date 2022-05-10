package com.keronei.survey.core.di

import android.content.Context
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.local.database.SurveyAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ): SurveyAppDatabase = SurveyAppDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providesQuestionnairesDao(surveyAppDatabase: SurveyAppDatabase): QuestionnaireDao =
        surveyAppDatabase.surveyDao()

    @Provides
    @Singleton
    fun providesSubmissionsDao(surveyAppDatabase: SurveyAppDatabase): SubmissionsDao =
        surveyAppDatabase.submissionDao()
}