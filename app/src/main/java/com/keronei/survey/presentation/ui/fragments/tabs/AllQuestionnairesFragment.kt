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
package com.keronei.survey.presentation.ui.fragments.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keronei.survey.R
import com.keronei.survey.core.ViewState
import com.keronei.survey.databinding.AllQuestionnairesFragmentBinding
import com.keronei.survey.presentation.ui.QuestionnairesRecyclerAdapter
import com.keronei.survey.presentation.ui.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class AllQuestionnairesFragment : Fragment() {

    lateinit var allQuestionnairesFragmentBinding: AllQuestionnairesFragmentBinding

    lateinit var allQuestionnairesRecyclerAdapter: QuestionnairesRecyclerAdapter

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allQuestionnairesFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.all_questionnaires_fragment,
            container,
            false
        )

        return allQuestionnairesFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allQuestionnairesRecyclerAdapter = QuestionnairesRecyclerAdapter({ item ->

            // If it's in the list, it cannot be empty
            mainViewModel.setSelectedQuestionnaire(
                item.id
            )

            if (mainViewModel.selectedQuestionnaireToFill != null) {
                findNavController().navigate(R.id.action_homeFragment_to_questionnaireFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Couldn't open ${item.id}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }, requireContext())

            setupRecycler()

            lifecycleScope.launchWhenResumed {
                mainViewModel.getAvailableQuestionnaires()
                mainViewModel.questionnaireStateFlow.collect { questionnaires ->
                    when (questionnaires) {
                        ViewState.Empty -> {
                            allQuestionnairesRecyclerAdapter.submitList(emptyList())

                            allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                                GONE
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.isEnabled = true

                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility =
                                VISIBLE
                            allQuestionnairesFragmentBinding.fillingInstructions.text =
                                getString(R.string.no_questionnaire)
                        }
                        is ViewState.Error -> {
                            allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                                GONE
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility =
                                VISIBLE
                            allQuestionnairesFragmentBinding.fillingInstructions.text =
                                questionnaires.exception.message
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.isEnabled = true
                        }
                        ViewState.Loading -> {
                            allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                                GONE
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility =
                                VISIBLE
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.isEnabled = false
                            allQuestionnairesFragmentBinding.fillingInstructions.text =
                                getString(R.string.loading)
                        }
                        is ViewState.Success -> {
                            allQuestionnairesRecyclerAdapter.submitList(questionnaires.presentations)

                            allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                                VISIBLE
                            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility = GONE
                            allQuestionnairesFragmentBinding.fillingInstructions.text =
                                getString(R.string.select_a_questionnaire_to_fill)
                        }
                    }
                }
            }

            allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.setOnClickListener {
                mainViewModel.getAvailableQuestionnaires()
            }
        }

        private fun setupRecycler() {
            with(allQuestionnairesFragmentBinding.availableQuestionnairesRecycler) {
                adapter = allQuestionnairesRecyclerAdapter
            }
        }
    }