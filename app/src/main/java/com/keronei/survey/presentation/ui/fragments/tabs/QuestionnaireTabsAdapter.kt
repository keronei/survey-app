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

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class QuestionnaireTabsAdapter(fm: Fragment?) : FragmentStateAdapter(
    fm!!
) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val fragmentTitleList: MutableList<String> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
}