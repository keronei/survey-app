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
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.data.models.SubmissionsDTOUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface SubmissionsDao {
    @Insert(onConflict = REPLACE)
    fun addSubmission(submissionsDTO: SubmissionsDTO): Long

    @Query("SELECT * FROM SubmissionsDTO")
    fun getSubmissions(): Flow<List<SubmissionsDTO>>

    @Update(entity = SubmissionsDTO::class) // only update sync status
    fun markSubmissionAsSynced(submissionsDTOUpdate: SubmissionsDTOUpdate): Int
}