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
import com.keronei.survey.R
import com.keronei.survey.databinding.SubmissionItemBinding
import com.keronei.survey.presentation.models.SubmissionPresentation

class SubmissionsRecyclerAdapter(
    private val itemSelected: (submission: SubmissionPresentation) -> Unit,
    private val context: Context
) :
    ListAdapter<SubmissionPresentation, SubmissionsRecyclerAdapter.RegionsViewHolder>(FilmDiffUtil()) {

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

    class RegionsViewHolder(val binding: SubmissionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(questionnaireDef: SubmissionPresentation, context: Context) {
            binding.submission = questionnaireDef

            if (questionnaireDef.synced) {
                binding.syncStatus.setImageResource(R.drawable.ic_baseline_cloud_sync_24)
            } else {
                binding.syncStatus.setImageResource(R.drawable.ic_baseline_sync_problem_24)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RegionsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemFilmBinding = SubmissionItemBinding.inflate(inflater, parent, false)

                return RegionsViewHolder(itemFilmBinding)
            }
        }
    }

    class FilmDiffUtil : DiffUtil.ItemCallback<SubmissionPresentation>() {
        override fun areItemsTheSame(
            oldItem: SubmissionPresentation,
            newItem: SubmissionPresentation
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SubmissionPresentation,
            newItem: SubmissionPresentation
        ): Boolean = oldItem == newItem
    }
}