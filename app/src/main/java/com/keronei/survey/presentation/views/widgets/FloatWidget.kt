package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.widget.EditText
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget

@SuppressLint("ViewConstructor")
class FloatWidget(context: Context, questionDefinition: QuestionDefinition) :
    StringWidget(context, questionDefinition) {

    override fun getAnswerField(): EditText {
        val editText = super.getAnswerField()
        editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        return editText
    }
}