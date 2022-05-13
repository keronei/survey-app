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
package com.keronei.survey.presentation.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.keronei.survey.R
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition

abstract class QuestionWidget(
    context: Context,
    private val questionDefinition: QuestionDefinition
) :
    FrameLayout(context) {

    private var containerView: ViewGroup

    init {
        id = generateViewId()

        containerView = inflate(
            context,
            R.layout.question_widget,
            this
        ).findViewById(R.id.question_widget_container)

        setQuestionLabel()
    }

    abstract fun getAnswer(): AnswerData?

    abstract fun saveCurrentAnswer(): Boolean

    private fun setQuestionLabel() {
        val questionText: TextView = findViewById(R.id.question_label)
        questionText.text = questionDefinition.questionText
    }

    fun getQuestionDefinition() = questionDefinition

    fun addAnswerView(view: View) {
        val answerView: ViewGroup =
            findViewById(R.id.answer_container) // questionWidgetBinding.answerContainer

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)

        answerView.addView(view, params)
    }
}