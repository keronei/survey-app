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
import android.view.View
import android.widget.EditText
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget

@SuppressLint("ViewConstructor")
open class StringWidget(context: Context, questionDefinition: QuestionDefinition) :
    QuestionWidget(context, questionDefinition) {
    private lateinit var answerEditText: EditText

    init {
        setupLayout()
    }

    private fun setupLayout() {
        answerEditText = getAnswerField()
        displayCurrentAnswer()
        addAnswerView(answerEditText)
    }

    private fun displayCurrentAnswer() {
        val currentAnswer = getQuestionDefinition().answerData
        currentAnswer?.let { answerData ->
            answerEditText.setText(answerData.response)
        }
    }

    open fun getAnswerField(): EditText {
        val editText = EditText(context)
        editText.id = View.generateViewId()

        return editText
    }

    override fun getAnswer(): AnswerData? {
        val answer = getAnswerText()
        return if (answer.isEmpty()) null else AnswerData(getQuestionDefinition().id, answer)
    }

    override fun saveCurrentAnswer(): Boolean {
        val answer = getAnswer() ?: return false

        getQuestionDefinition().answerData = answer
        return true
    }

    private fun getAnswerText(): String = answerEditText.text.toString()
}