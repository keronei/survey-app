package com.keronei.survey.domain.models

import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.AnswerType
import com.keronei.survey.core.QuestionType

data class QuestionDefinition(
    val id: String,
    val questionType: QuestionType,
    val answerType: AnswerType,
    val questionText: String,
    val options: List<ChoiceOption>,
    val nextQuestion: String?,
    var answerData: AnswerData?
)
