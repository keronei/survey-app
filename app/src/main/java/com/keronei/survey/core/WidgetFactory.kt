package com.keronei.survey.core

import android.content.Context
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget
import com.keronei.survey.presentation.views.widgets.FloatWidget
import com.keronei.survey.presentation.views.widgets.SingleLineStringWidget

class WidgetFactory(private val context: Context) {
    fun createWidgetForQuestion(questionDefinition: QuestionDefinition): QuestionWidget {
        return when(questionDefinition.answerType){
            AnswerType.SINGLE_LINE_TEXT -> {
                SingleLineStringWidget(context, questionDefinition)
            }
            AnswerType.FLOAT -> {
                FloatWidget(context, questionDefinition)
            }
        }
    }
}