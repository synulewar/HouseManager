package com.synowkrz.housemanager.babyTask.babyMainMenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.databinding.BabyGridViewItemBinding

class BabyGridAdpater(val onClickListener: OnClickListener,
                      val onLongClickListener: OnLongClickListener = OnLongClickListener{ true }) :
    ListAdapter<BabyProfile, BabyGridAdpater.BabyViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        return BabyGridAdpater.BabyViewHolder(
            BabyGridViewItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener{
            onLongClickListener.onLongClick(item)
        }
        holder.bind(item)
    }


    class BabyViewHolder(private var binding: BabyGridViewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(babyProfile: BabyProfile) {
            binding.property = babyProfile
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<BabyProfile>() {
        override fun areItemsTheSame(oldItem: BabyProfile, newItem: BabyProfile): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BabyProfile, newItem: BabyProfile): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (babyProfile: BabyProfile) -> Unit) {
        fun onClick(taskGridItem: BabyProfile) = clickListener(taskGridItem)
    }

    class OnLongClickListener(val longClickListener: (babyProfile: BabyProfile) -> Boolean) {
        fun onLongClick(taskGridItem: BabyProfile) = longClickListener(taskGridItem)
    }
}