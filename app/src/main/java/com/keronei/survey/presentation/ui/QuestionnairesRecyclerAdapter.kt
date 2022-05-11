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
package com.keronei.survey.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keronei.survey.databinding.QuestionnaireItemBinding
import com.keronei.survey.presentation.models.QuestionnaireDefPresentation

class QuestionnairesRecyclerAdapter(
    private val itemSelected: (questionnaire: QuestionnaireDefPresentation) -> Unit,
    private val context: Context
) :
    ListAdapter<QuestionnaireDefPresentation, QuestionnairesRecyclerAdapter.RegionsViewHolder>(
        FilmDiffUtil()
    ) {

    var untouchedList = listOf<QuestionnaireDefPresentation>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionsViewHolder = RegionsViewHolder.from(parent)

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        val defPresentation = getItem(position)
        holder.bind(defPresentation, context)

        holder.binding.root.setOnClickListener {
            itemSelected(defPresentation)
        }
    }

    fun modifyList(list: List<QuestionnaireDefPresentation>) {
        untouchedList = list
        submitList(list)
    }

    class RegionsViewHolder(val binding: QuestionnaireItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questionnaireDef: QuestionnaireDefPresentation, context: Context) {
            binding.questionnaire = questionnaireDef

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RegionsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemFilmBinding = QuestionnaireItemBinding.inflate(inflater, parent, false)

                return RegionsViewHolder(itemFilmBinding)
            }
        }
    }

    class FilmDiffUtil : DiffUtil.ItemCallback<QuestionnaireDefPresentation>() {
        override fun areItemsTheSame(
            oldItem: QuestionnaireDefPresentation,
            newItem: QuestionnaireDefPresentation
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: QuestionnaireDefPresentation,
            newItem: QuestionnaireDefPresentation
        ): Boolean = oldItem == newItem
    }
}