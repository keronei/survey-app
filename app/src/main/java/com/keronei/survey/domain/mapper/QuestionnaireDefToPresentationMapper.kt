package com.keronei.survey.domain.mapper

import com.keronei.survey.domain.models.QuestionnaireDef
import com.keronei.survey.presentation.models.QuestionnaireDefPresentation

class QuestionnaireDefToPresentationMapper : Mapper<QuestionnaireDef, QuestionnaireDefPresentation>() {
    override fun map(input: QuestionnaireDef): QuestionnaireDefPresentation {
        return QuestionnaireDefPresentation(input.id,
            "${input.questions.size} questions.",
            "0 submissions",
            "15.05.2022")
    }
}