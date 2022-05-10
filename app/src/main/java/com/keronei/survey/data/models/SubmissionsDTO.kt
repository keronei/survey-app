package com.keronei.survey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = QuestionnaireDefDTO::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("questionnaireId"),
        onDelete = RESTRICT
    )]
)
data class SubmissionsDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(defaultValue = "(strftime('%s','now'))")
    val submissionDate: Long,
    @ColumnInfo(index = true)
    val questionnaireId: String,
    val submissionName: String,
    val submissionAsJson: String,
    @ColumnInfo(defaultValue = "0")
    val synced: Boolean
)