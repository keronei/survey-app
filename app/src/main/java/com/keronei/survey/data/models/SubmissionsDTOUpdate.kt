package com.keronei.survey.data.models

import androidx.room.PrimaryKey

data class SubmissionsDTOUpdate(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val synced: Boolean
)