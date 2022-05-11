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
package com.keronei.survey.core

import android.app.Activity
import android.content.Context
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget
import com.keronei.survey.presentation.views.widgets.FloatWidget
import com.keronei.survey.presentation.views.widgets.ImageCaptureWidget
import com.keronei.survey.presentation.views.widgets.SelectSingleWidget
import com.keronei.survey.presentation.views.widgets.SingleLineStringWidget

class WidgetFactory(private val context: Context, private val activity: Activity) {
    fun createWidgetForQuestion(questionDefinition: QuestionDefinition): QuestionWidget {
        return when (questionDefinition.questionType) {
            QuestionType.FREE_TEXT -> {
                stringWidget(questionDefinition)
            }
            QuestionType.SELECT_ONE -> SelectSingleWidget(context, questionDefinition)
            QuestionType.TYPE_VALUE -> {
                stringWidget(questionDefinition)
            }
            QuestionType.IMAGE_CAPTURE -> {
                ImageCaptureWidget(activity, context, questionDefinition)
            }
        }
    }

    private fun stringWidget(questionDefinition: QuestionDefinition) =
        when (questionDefinition.answerType) {
            AnswerType.SINGLE_LINE_TEXT -> SingleLineStringWidget(context, questionDefinition)
            AnswerType.FLOAT -> FloatWidget(context, questionDefinition)
            AnswerType.IMAGE_PATH -> ImageCaptureWidget(activity, context, questionDefinition)
        }
}