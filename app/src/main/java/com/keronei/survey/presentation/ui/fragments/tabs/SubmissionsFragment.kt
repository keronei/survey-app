package com.keronei.survey.presentation.ui.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.keronei.survey.R
import com.keronei.survey.databinding.SubmissionsFragmentBinding
import com.keronei.survey.presentation.ui.SubmissionsRecyclerAdapter
import com.keronei.survey.presentation.ui.viewmodel.SubmissionsViewModel
import kotlinx.coroutines.flow.collect

class SubmissionsFragment : Fragment() {

    private val viewModel: SubmissionsViewModel by activityViewModels()

    private lateinit var submissionsFragmentBinding: SubmissionsFragmentBinding

    private lateinit var adapter: SubmissionsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        submissionsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.submissions_fragment, container, false)

        return submissionsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SubmissionsRecyclerAdapter({ item ->
            val message = if (item.synced) {
                "${item.submissionName} sync successful."
            } else {
                "${item.submissionName} has not been synced."
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }, requireContext())

        setupSubmissionsRecycler()

        lifecycleScope.launchWhenResumed {
            viewModel.getSubmissions().collect { submissions ->
                adapter.submitList(submissions)

                if (submissions.isEmpty()) {
                    submissionsFragmentBinding.noSubmissionsText.visibility = VISIBLE
                    submissionsFragmentBinding.submissionsRecycler.visibility = GONE
                } else {
                    submissionsFragmentBinding.noSubmissionsText.visibility = GONE
                    submissionsFragmentBinding.submissionsRecycler.visibility = VISIBLE
                }
            }
        }
    }

    private fun setupSubmissionsRecycler() {
        with(submissionsFragmentBinding.submissionsRecycler) {
            adapter = adapter
        }
    }


}