package com.synowkrz.housemanager.homeTaskList.homeTaskDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.DoneTaskListItemBinding
import com.synowkrz.housemanager.databinding.MainTaskListItemBinding
import com.synowkrz.housemanager.homeTaskList.model.DoneTask
import com.synowkrz.housemanager.homeTaskList.model.HomeTask

class DoneTaskAdpater : ListAdapter<DoneTask, DoneTaskAdpater.DoneTaskItemViewHolder>(DiffCallback)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneTaskItemViewHolder {
        return DoneTaskItemViewHolder(DoneTaskListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DoneTaskItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }


    class DoneTaskItemViewHolder(val binding : DoneTaskListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(doneTask : DoneTask) {
            Log.d(TAG, "onBind $doneTask" )
            binding.property = doneTask
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DoneTask>() {
        override fun areItemsTheSame(oldItem: DoneTask, newItem: DoneTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DoneTask, newItem: DoneTask): Boolean {
            return oldItem.id == newItem.id
        }
    }
}