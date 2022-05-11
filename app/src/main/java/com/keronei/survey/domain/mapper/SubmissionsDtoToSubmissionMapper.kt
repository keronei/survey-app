package com.keronei.survey.domain.mapper

import com.keronei.survey.data.models.SubmissionsDTO
import com.keronei.survey.domain.models.Submission

class SubmissionsDtoToSubmissionMapper : Mapper<SubmissionsDTO, Submission>() {
    override fun map(input: SubmissionsDTO): Submission {
        return Submission(
            input.id,
            input.submissionDate,
            input.submissionName,
            input.questionnaireId,
            input.synced
        )

    }
}