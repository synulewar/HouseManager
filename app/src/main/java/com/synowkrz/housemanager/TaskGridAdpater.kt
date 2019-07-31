package com.synowkrz.housemanager


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.TaskGridViewItemBinding
import com.synowkrz.housemanager.model.TaskGridItem

class TaskGridAdpater(val onClickListener: OnClickListener,
                      val onLongClickListener: OnLongClickListener = OnLongClickListener{ true }) :
    ListAdapter<TaskGridItem, TaskGridAdpater.TaskGridItemViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskGridItemViewHolder {
        return TaskGridItemViewHolder(
            TaskGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: TaskGridItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener{
            onLongClickListener.onLongClick(item)
        }
        holder.bind(item)
    }


    class TaskGridItemViewHolder(private var binding: TaskGridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(taskGridItem: TaskGridItem) {
            binding.property = taskGridItem
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<TaskGridItem>() {
        override fun areItemsTheSame(oldItem: TaskGridItem, newItem: TaskGridItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TaskGridItem, newItem: TaskGridItem): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (taskGridItem: TaskGridItem) -> Unit) {
        fun onClick(taskGridItem: TaskGridItem) = clickListener(taskGridItem)
    }

    class OnLongClickListener(val longClickListener: (taskGridItem: TaskGridItem) -> Boolean) {
        fun onLongClick(taskGridItem: TaskGridItem) = longClickListener(taskGridItem)
    }
}