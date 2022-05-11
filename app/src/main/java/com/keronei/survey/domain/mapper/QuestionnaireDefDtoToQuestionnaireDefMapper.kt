/*
 * Copyright 2022 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.survey.domain.mapper

import com.keronei.survey.data.models.QuestionnaireSubDTO
import com.keronei.survey.domain.models.QuestionnaireDef
import java.util.*

class QuestionnaireDefDtoToQuestionnaireDefMapper : Mapper<QuestionnaireSubDTO, QuestionnaireDef>() {
    override fun map(input: QuestionnaireSubDTO): QuestionnaireDef {
        return QuestionnaireDef(
            input.id,
            input.language,
            QuestionDefDtoToQuestionDefinition().mapList(input.questions),
            Date(input.downloadDate),
            input.submissionsCount
        )
    }
}