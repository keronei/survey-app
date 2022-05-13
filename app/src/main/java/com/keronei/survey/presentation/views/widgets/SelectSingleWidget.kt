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
import android.widget.RadioButton
import android.widget.RadioGroup
import com.keronei.survey.R
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget
import timber.log.Timber

@SuppressLint("ViewConstructor")
class SelectSingleWidget(context: Context, questionDefinition: QuestionDefinition) :
    QuestionWidget(context, questionDefinition) {
    private lateinit var radioGroup: RadioGroup

    private val cacheSelector = mutableMapOf<Int, String>()

    init {
        cacheSelector.clear()
        setupLayout()
    }

    private fun setupLayout() {
        addAnswerView(getAnswerField())
    }

    private fun getAnswerField(): View {
        val view: View = inflate(context, R.layout.select_one_widget, null)
        radioGroup = view.findViewById(R.id.radio_group)

        val questionDetails = getQuestionDefinition()

        val options = questionDetails.options

        val previousAnswer = questionDetails.answerData


        for (option in options) {
            val radioButton = RadioButton(context)

            radioButton.text = option.displayText
            radioButton.tag = option.value
            val generatedId = View.generateViewId()
            radioButton.id = generatedId
            cacheSelector[generatedId] = option.value
            radioButton.isChecked = option.selected

            radioGroup.addView(radioButton)


            if (previousAnswer != null) {
                if (option.value == previousAnswer.response) {
                    radioButton.isChecked = true
                }
            }
        }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectionItem = options.firstOrNull { item ->
                    item.value == cacheSelector[checkedId]

                }

                selectionItem?.selected = true

                // unselect others
                options.forEach { choice ->
                    if (choice.value != selectionItem?.value) {
                        choice.selected = false
                    }

                }

            }

        return view
    }

    override fun getAnswer(): AnswerData? {
        val selectedOption = radioGroup.checkedRadioButtonId

        val currentSelection = cacheSelector[selectedOption]

        return if (currentSelection == null) null else AnswerData(
            getQuestionDefinition().id,
            currentSelection
        )
    }

    override fun saveCurrentAnswer(): Boolean {
        val answer = getAnswer() ?: return false

        getQuestionDefinition().answerData = answer
        return true
    }
}