package com.keronei.survey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionnaireDefDTO(
    @ColumnInfo(index = true)
    @PrimaryKey
    val id: String,
    val language: String,
    val questions: List<QuestionDefDTO>,
    val startQuestionId: String,
    @ColumnInfo(defaultValue = "(strftime('%s','now'))")
    val downloadDate: Long
)
