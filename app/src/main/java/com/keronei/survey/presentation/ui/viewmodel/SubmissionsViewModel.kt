package com.keronei.survey.presentation.ui.fragments.tabs

import androidx.lifecycle.ViewModel
import com.keronei.survey.presentation.models.SubmissionPresentation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SubmissionsViewModel : ViewModel() {
    fun getSubmissions(): Flow<List<SubmissionPresentation>> {
        return flowOf(emptyList())
    }
}