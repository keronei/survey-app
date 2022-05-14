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
package com.keronei.survey.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = QuestionnaireDefDTO::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("questionnaireId"),
            onDelete = RESTRICT
        )
    ]
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