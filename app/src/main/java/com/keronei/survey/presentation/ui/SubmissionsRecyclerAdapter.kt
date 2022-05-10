package com.keronei.survey.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keronei.survey.R
import com.keronei.survey.databinding.QuestionnaireItemBinding
import com.keronei.survey.databinding.SubmissionItemBinding
import com.keronei.survey.presentation.models.QuestionnaireDefPresentation
import com.keronei.survey.presentation.models.SubmissionPresentation

class SubmissionsRecyclerAdapter(
    private val itemSelected: (submission: SubmissionPresentation) -> Unit,
    private val context: Context
) :
    ListAdapter<SubmissionPresentation, SubmissionsRecyclerAdapter.RegionsViewHolder>(FilmDiffUtil()) {

    private var untouchedList = listOf<SubmissionPresentation>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegionsViewHolder {
        return RegionsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RegionsViewHolder, position: Int) {
        val defPresentation = getItem(position)
        holder.bind(defPresentation, context)

        holder.binding.root.setOnClickListener {
            itemSelected(defPresentation)
        }
    }

    fun modifyList(list: List<SubmissionPresentation>) {
        untouchedList = list
        submitList(list)
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
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SubmissionPresentation,
            newItem: SubmissionPresentation
        ): Boolean {
            return oldItem == newItem
        }

    }
}