package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import com.keronei.survey.core.AnswerData
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget

@SuppressLint("ViewConstructor")
class FloatWidget(context: Context, questionDefinition: QuestionDefinition) :
    QuestionWidget(context, questionDefinition) {
    override fun getAnswer(): AnswerData {
        TODO("Not yet implemented")
    }
}