package com.synowkrz.housemanager.homeTaskList.oneShotTask

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.OneShotListItemBinding
import com.synowkrz.housemanager.homeTaskList.model.OneShotTask

class OneShotTaskAdapter(val onClickListener : OnOneShotTaskListener, val onLongClickListener : OnOneShotTaskLongListener, val app: Application) : ListAdapter<OneShotTask, OneShotTaskAdapter.OneShotViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneShotViewHolder {
        return OneShotViewHolder(OneShotListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OneShotViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.checkBox.setOnClickListener {
            onClickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener.onClick(item)
        }

        holder.onBind(item)
    }

    class OneShotViewHolder(val binding: OneShotListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(oneShotTask: OneShotTask) {
            binding.property = oneShotTask
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<OneShotTask>() {
        override fun areItemsTheSame(oldItem: OneShotTask, newItem: OneShotTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OneShotTask, newItem: OneShotTask): Boolean {
            return oldItem.id == newItem.id && oldItem.dueDate == newItem.dueDate && oldItem.done == newItem.done
        }

    }

    class OnOneShotTaskListener(val clickListener : (oneShotTask : OneShotTask) -> Unit) {
        fun onClick(oneShotTask: OneShotTask) = clickListener(oneShotTask)
    }

    class OnOneShotTaskLongListener(val clickListener : (oneShotTask : OneShotTask) -> Boolean) {
        fun onClick(oneShotTask: OneShotTask) = clickListener(oneShotTask)
    }
}