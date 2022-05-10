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
        inflater: LayoutInflater, container: ViewGroup?,
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
            mainViewModel.setSelectedQuestionnaire(mainViewModel.getQuestionnaireById(item.id))

            if (mainViewModel.selectedQuestionnaireToFill != null) {
                findNavController().navigate(R.id.action_homeFragment_to_questionnaireFragment)
            } else {
                Toast.makeText(requireContext(), "Couldn't open ${item.id}", Toast.LENGTH_SHORT)
                    .show()
            }
        }, requireContext())

        setupRecycler()

        lifecycleScope.launchWhenResumed {
            mainViewModel.getAvailableQuestionnaires().collect { questionnaires ->
                allQuestionnairesRecyclerAdapter.submitList(questionnaires)

                if (questionnaires.isEmpty()) {
                    allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                        GONE
                    allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility = VISIBLE
                    allQuestionnairesFragmentBinding.fillingInstructions.text =
                        getString(R.string.no_questionnaire)
                } else {
                    allQuestionnairesFragmentBinding.availableQuestionnairesRecycler.visibility =
                        VISIBLE
                    allQuestionnairesFragmentBinding.btnDownloadQuestionnaires.visibility = GONE
                    allQuestionnairesFragmentBinding.fillingInstructions.text =
                        getString(R.string.select_a_questionnaire_to_fill)
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