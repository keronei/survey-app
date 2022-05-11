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

    @Update(entity = SubmissionsDTO::class)// only update sync status
    fun markSubmissionAsSynced(submissionsDTOUpdate: SubmissionsDTOUpdate): Int
}