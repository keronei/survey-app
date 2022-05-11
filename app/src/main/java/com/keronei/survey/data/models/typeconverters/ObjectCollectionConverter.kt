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
package com.keronei.survey.data.models.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.keronei.survey.data.models.OptionDTO
import com.keronei.survey.data.models.QuestionDefDTO

class ObjectCollectionConverter {
    private val gsonInstance = Gson()

    @TypeConverter
    fun fromOptionsList(options: List<OptionDTO>): String = gsonInstance.toJson(options)

    @TypeConverter
    fun toOptionsList(json: String?): List<OptionDTO> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<OptionDTO>::class.java).asList()
        }
    }

    @TypeConverter
    fun fromQuestionsList(questions: List<QuestionDefDTO>): String = gsonInstance.toJson(questions)

    @TypeConverter
    fun toQuestionsList(json: String?): List<QuestionDefDTO> {
        return if (json == "" || json == null) {
            emptyList()
        } else {
            gsonInstance.fromJson(json, Array<QuestionDefDTO>::class.java).asList()
        }
    }
}