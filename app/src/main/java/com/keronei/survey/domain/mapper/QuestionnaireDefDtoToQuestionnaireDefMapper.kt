package com.keronei.survey.domain.mapper

import com.keronei.survey.data.models.QuestionnaireDefDTO
import com.keronei.survey.domain.models.QuestionnaireDef
import java.util.*

class QuestionnaireDefDtoToQuestionnaireDefMapper : Mapper<QuestionnaireDefDTO, QuestionnaireDef>() {
    override fun map(input: QuestionnaireDefDTO): QuestionnaireDef {
        return QuestionnaireDef(
            input.id,
            input.language,
            QuestionDefDtoToQuestionDefinition().mapList(input.questions),
            Date(input.downloadDate))
    }

}