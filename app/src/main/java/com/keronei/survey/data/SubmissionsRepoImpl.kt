package com.keronei.survey.data

import com.keronei.survey.data.local.LocalDataSource
import com.keronei.survey.data.remote.NetworkDataSource
import com.keronei.survey.domain.models.Submission
import com.keronei.survey.domain.repositories.SubmissionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubmissionsRepoImpl @Inject constructor(
    remoteDataSource: NetworkDataSource,
    localDataSource: LocalDataSource
) : SubmissionsRepository {
    override fun getSubmissions(): Flow<List<Submission>> {
        TODO("Not yet implemented")
    }

    override fun submitCurrentResponses() {
        TODO("Not yet implemented")
    }
}