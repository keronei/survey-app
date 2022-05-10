package com.keronei.survey.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.keronei.survey.R
import com.keronei.survey.core.QuestionnaireController.EVENT_BEGINNING_QUESTIONNAIRE
import com.keronei.survey.core.QuestionnaireController.EVENT_END_QUESTIONNAIRE
import com.keronei.survey.core.QuestionnaireController.EVENT_QUESTION
import com.keronei.survey.core.QuestionnaireController.UNKNOWN_EVENT
import com.keronei.survey.core.WidgetFactory
import com.keronei.survey.databinding.FragmentQuestionnaireBinding
import com.keronei.survey.presentation.ui.viewmodel.MainViewModel
import com.keronei.survey.presentation.views.QuestionWidget
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class QuestionnaireFragment : Fragment() {

    private lateinit var fragmentQuestionnaireBinding: FragmentQuestionnaireBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var widgetFactory: WidgetFactory

    private var currentQuestionOnDisplay: QuestionWidget? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentQuestionnaireBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_questionnaire, container, false)

        return fragmentQuestionnaireBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetFactory = WidgetFactory(requireActivity())
        // On start, the user is starting a questionnaire and is in Q1
        val initialQuestion = mainViewModel.getCurrentQuestion()

        if (initialQuestion != null) {

            val questionView = widgetFactory.createWidgetForQuestion(initialQuestion)

            fragmentQuestionnaireBinding.questionHolder.addView(questionView)

            currentQuestionOnDisplay = questionView

        }

        handleNavigationButtonsClick()
    }

    private fun handleNavigationButtonsClick() {
        fragmentQuestionnaireBinding.btnBack.setOnClickListener {
            val currentEvent = mainViewModel.getCurrentEvent()

            val answerOk = validateCurrentAnswer()

            if (!answerOk) {
                return@setOnClickListener
            }

            val currentQuestion = mainViewModel.nextQuestion()

            if (currentEvent != EVENT_QUESTION) {
                handleEvent(currentEvent)
            } else {
                if (currentQuestion != null) {
                    val widgetToDisplay = widgetFactory.createWidgetForQuestion(currentQuestion)

                    showQuestionView(widgetToDisplay)
                }
            }
        }

        fragmentQuestionnaireBinding.btnNext.setOnClickListener {
            val answerOk = validateCurrentAnswer()

            if (!answerOk) {
                return@setOnClickListener
            }

            val previousQuestion = mainViewModel.previousQuestion()

            val currentEvent = mainViewModel.getCurrentEvent()
            if (currentEvent != EVENT_QUESTION) {
                handleEvent(currentEvent)
            } else {
                if (previousQuestion != null) {
                    val question = widgetFactory.createWidgetForQuestion(previousQuestion)
                    showQuestionView(question)
                }
            }
        }
    }

    private fun showQuestionView(question: QuestionWidget) {
        fragmentQuestionnaireBinding.questionHolder.removeAllViews()
        fragmentQuestionnaireBinding.questionHolder.addView(question)
        currentQuestionOnDisplay = question
    }

    private fun validateCurrentAnswer(): Boolean {
        if (currentQuestionOnDisplay != null) {
            val valueIsOK = currentQuestionOnDisplay!!.saveCurrentAnswer()

            if (!valueIsOK) {
                Toast.makeText(context, "Response is empty.", Toast.LENGTH_SHORT).show()
                return false
            }
            mainViewModel.saveCurrentAnswer(currentQuestionOnDisplay!!.getQuestionDefinition())
        }
        return true

    }

    private fun handleEvent(event: Int) {
        when (event) {
            EVENT_BEGINNING_QUESTIONNAIRE -> {

            }

            EVENT_END_QUESTIONNAIRE -> {
                fragmentQuestionnaireBinding.btnNext.text = getString(R.string.finish)
            }

            UNKNOWN_EVENT -> {

            }
        }
    }


}