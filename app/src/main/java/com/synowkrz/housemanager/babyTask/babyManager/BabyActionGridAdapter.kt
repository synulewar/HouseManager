package com.synowkrz.housemanager.babyTask.babyManager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.babyTask.model.BasicBabyEvent
import com.synowkrz.housemanager.databinding.BabyActionGridViewBinding

class BabyActionGridAdapter(val onClickListener: OnClickListener) :
    ListAdapter<BasicBabyEvent, BabyActionGridAdapter.BabyActionHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyActionHolder {
        return BabyActionHolder(
            BabyActionGridViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BabyActionHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }


    companion object DiffCallback: DiffUtil.ItemCallback<BasicBabyEvent>() {
        override fun areItemsTheSame(oldItem: BasicBabyEvent, newItem: BasicBabyEvent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BasicBabyEvent, newItem: BasicBabyEvent): Boolean {
            return oldItem.eventType == newItem.eventType
        }
    }

    class BabyActionHolder(private var binding: BabyActionGridViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(basicBabyEvent: BasicBabyEvent) {
            binding.property = basicBabyEvent
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (basicBabyEvent: BasicBabyEvent) -> Unit) {
        fun onClick(basicBabyEvent: BasicBabyEvent) = clickListener(basicBabyEvent)
    }
}