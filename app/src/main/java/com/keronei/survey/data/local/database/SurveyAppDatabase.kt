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
package com.keronei.survey.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keronei.survey.data.local.dao.SubmissionsDao
import com.keronei.survey.data.local.dao.QuestionnaireDao
import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.typeconverters.ObjectCollectionConverter

@Database(
    entities = [QuestionnaireDefDTO::class, SubmissionsDTO::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(ObjectCollectionConverter::class)
abstract class SurveyAppDatabase : RoomDatabase() {
    abstract fun surveyDao(): QuestionnaireDao

    abstract fun submissionDao(): SubmissionsDao

    companion object {

        @Volatile
        private var databaseInstance: SurveyAppDatabase? = null

        fun buildDatabase(
            context: Context
        ): SurveyAppDatabase {
            return databaseInstance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SurveyAppDatabase::class.java,
                    "surveya_app_db.db"
                )
                    .build()
                databaseInstance = instance

                instance
            }
        }
    }
}