package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.keronei.survey.core.AnswerData
import com.keronei.survey.databinding.SelectOneWidgetBinding
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget

@SuppressLint("ViewConstructor")
class SelectSingleWidget(context: Context, questionDefinition: QuestionDefinition) :
    QuestionWidget(context, questionDefinition) {
    private lateinit var radioGroup: RadioGroup
    private val selectSingleWidgetBinding: SelectOneWidgetBinding =
        SelectOneWidgetBinding.bind(this)

    private val cacheSelector = mutableMapOf<Int, String>()

    init {
        cacheSelector.clear()
        setupLayout()
    }

    private fun setupLayout() {
        addAnswerView(getAnswerField())
    }

    private fun getAnswerField(): View {
        radioGroup = selectSingleWidgetBinding.radioGroup

        val questionDetails = getQuestionDefinition()

        val options = questionDetails.options

        for (option in options) {
            val radioButton = RadioButton(context)

            radioButton.text = option.displayText
            radioButton.tag = option.value
            radioButton.isSelected = option.selected
            val generatedId = View.generateViewId()
            radioButton.id = generatedId
            cacheSelector[generatedId] = option.value
            radioGroup.addView(radioButton)
        }

        return radioGroup
    }

    override fun getAnswer(): AnswerData? {
        val selectedOption = radioGroup.checkedRadioButtonId

        val currentSelection = cacheSelector[selectedOption]

        return if (currentSelection == null) null else AnswerData(getQuestionDefinition().id, currentSelection)

    }

    override fun saveCurrentAnswer(): Boolean {
        val answer = getAnswer() ?: return false

        getQuestionDefinition().answerData = answer
        return true
    }


}