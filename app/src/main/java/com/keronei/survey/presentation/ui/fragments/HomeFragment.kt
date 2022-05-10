package com.keronei.survey.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.keronei.survey.R
import com.keronei.survey.databinding.HomeFragmentBinding
import com.keronei.survey.presentation.ui.fragments.tabs.AllQuestionnairesFragment
import com.keronei.survey.presentation.ui.fragments.tabs.QuestionnaireTabsAdapter
import com.keronei.survey.presentation.ui.fragments.tabs.SubmissionsFragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeFragmentBinding: HomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        val tabs = homeFragmentBinding.tabLayout
        val viewPager = homeFragmentBinding.viewpager

        val adapter = QuestionnaireTabsAdapter(this)

        adapter.addFragment(AllQuestionnairesFragment(), "Fill New")
        adapter.addFragment(SubmissionsFragment(), "Submissions")

        viewPager.adapter = adapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            viewPager.setCurrentItem(tab.position, true)
            tab.text = adapter.getPageTitle(position)
        }.attach()

        return homeFragmentBinding.root
    }


}