package com.keronei.survey.presentation.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.keronei.survey.core.AnswerData
import com.keronei.survey.databinding.QuestionWidgetBinding
import com.keronei.survey.domain.models.QuestionDefinition

abstract class QuestionWidget(context: Context,private val questionDefinition: QuestionDefinition) :
    FrameLayout(context) {

    private var containerView: ViewGroup

    private var questionWidgetBinding: QuestionWidgetBinding = QuestionWidgetBinding.bind(this)

    init {

        containerView = questionWidgetBinding.answerContainer

        val answerView = onCreateAnswerView()

        answerView?.let { providedView ->
            addAnswerView(providedView)
        }
    }

    abstract fun getAnswer() : AnswerData?

    fun setQuestionLabel() {
    }

    fun getQuestionDefinition() = questionDefinition

     fun addAnswerView(view: View) {
        val answerView = questionWidgetBinding.answerContainer

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)

        answerView.addView(view, params)

    }

    protected fun onCreateAnswerView(): View? {
        return null
    }

}