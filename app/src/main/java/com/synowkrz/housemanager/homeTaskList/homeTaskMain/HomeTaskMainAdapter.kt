package com.synowkrz.housemanager.homeTaskList.homeTaskMain

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.MainTaskListItemBinding
import com.synowkrz.housemanager.getColorByExpiredTime
import com.synowkrz.housemanager.homeTaskList.model.HomeTask

class HomeTaskMainAdapter(val onClickListener : OnHomeTaskClickListener, val app: Application) : ListAdapter<HomeTask, HomeTaskMainAdapter.HomeTaskItemViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskItemViewHolder {
        return HomeTaskItemViewHolder(MainTaskListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HomeTaskItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setBackgroundColor(getColorByExpiredTime(item, app))

        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.onBind(item)
    }


    class HomeTaskItemViewHolder(val binding : MainTaskListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(homeTask : HomeTask) {
            Log.d(TAG, "onBind $homeTask" )
            binding.property = homeTask
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HomeTask>() {
        override fun areItemsTheSame(oldItem: HomeTask, newItem: HomeTask): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: HomeTask, newItem: HomeTask): Boolean {
            return oldItem.name == newItem.name && oldItem.dueDate == newItem.dueDate && oldItem.daysExceeded == newItem.daysExceeded
        }

    }

    class OnHomeTaskClickListener(val clickListener : (homeTask : HomeTask) -> Unit) {
        fun onClick(homeTask: HomeTask) = clickListener(homeTask)
    }

}