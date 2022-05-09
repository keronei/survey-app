package com.keronei.survey.presentation.ui.fragments.tabs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keronei.survey.R

class AllQustionnairesFragment : Fragment() {

    companion object {
        fun newInstance() = AllQustionnairesFragment()
    }

    private lateinit var viewModel: AllQustionnairesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_qustionnaires_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllQustionnairesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}