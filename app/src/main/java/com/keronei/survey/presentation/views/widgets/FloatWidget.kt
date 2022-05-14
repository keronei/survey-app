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
package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import com.keronei.survey.domain.models.QuestionDefinition
import java.util.regex.Pattern

@SuppressLint("ViewConstructor")
class FloatWidget(context: Context, questionDefinition: QuestionDefinition) :
    StringWidget(context, questionDefinition) {

    override fun getAnswerField(): EditText {
        val editText = super.getAnswerField()
        editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER

        editText.limitDecimalPlaces(1)

        return editText
    }

    private fun EditText.limitDecimalPlaces(maxDecimalPlaces: Int) {
        filters += InputFilter { source, _, _, dest, dstart, dend ->
            val value = if (source.isEmpty()) {
                dest.removeRange(dstart, dend)
            } else {
                StringBuilder(dest).insert(dstart, source)
            }
            val matcher =
                Pattern.compile("([1-9][0-9]*)|([1-9][0-9]*\\.[0-9]{0,$maxDecimalPlaces})|(\\.[0-9]{0,$maxDecimalPlaces})")
                    .matcher(value)
            if (!matcher.matches()) "" else null
        }
    }
}