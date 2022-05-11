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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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