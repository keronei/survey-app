package com.keronei.survey.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keronei.survey.domain.repositories.SubmissionsRepository
import com.keronei.survey.presentation.models.SubmissionPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SubmissionsViewModel(private val submissionsRepository: SubmissionsRepository) : ViewModel() {
    fun getSubmissions(): Flow<List<SubmissionPresentation>> = submissionsRepository.getSubmissions()
}