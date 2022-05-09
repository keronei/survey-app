package com.keronei.survey.core

import android.content.Context
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget
import com.keronei.survey.presentation.views.widgets.FloatWidget
import com.keronei.survey.presentation.views.widgets.SelectSingleWidget
import com.keronei.survey.presentation.views.widgets.SingleLineStringWidget

class WidgetFactory(private val context: Context) {
    fun createWidgetForQuestion(questionDefinition: QuestionDefinition): QuestionWidget {
        return when (questionDefinition.questionType) {
            QuestionType.FREE_TEXT -> {
                stringWidget(questionDefinition)
            }
            QuestionType.SELECT_ONE -> SelectSingleWidget(context, questionDefinition)
            QuestionType.TYPE_VALUE -> {
                stringWidget(questionDefinition)
            }
        }
    }

    private fun stringWidget(questionDefinition: QuestionDefinition) =
        when (questionDefinition.answerType) {
            AnswerType.SINGLE_LINE_TEXT -> SingleLineStringWidget(context, questionDefinition)
            AnswerType.FLOAT -> FloatWidget(context, questionDefinition)
        }
}