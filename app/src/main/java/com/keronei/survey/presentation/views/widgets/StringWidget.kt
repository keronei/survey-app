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
        editText.setSingleLine()

        return editText
    }

    override fun getAnswer(): AnswerData? {
        val answer = getAnswerText()
        return if (answer.isEmpty()) null else AnswerData(answer)
    }

    private fun getAnswerText(): String {
        return answerEditText.text.toString()
    }
}