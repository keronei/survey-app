package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.EditText
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget

@SuppressLint("ViewConstructor")
class SingleLineStringWidget(context: Context, questionDefinition: QuestionDefinition) :
    StringWidget(context, questionDefinition) {

    override fun getAnswerField(): EditText {
        val editText = super.getAnswerField()
        editText.setSingleLine()
        return editText
    }
}