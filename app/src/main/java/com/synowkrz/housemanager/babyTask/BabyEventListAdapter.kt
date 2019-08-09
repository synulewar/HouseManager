package com.synowkrz.housemanager.babyTask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.babyTask.model.BabyEvent
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.databinding.BabyEventListItemBinding
import com.synowkrz.housemanager.transformFeedingTypeIntoImageResource
import java.text.SimpleDateFormat
import java.util.*

class BabyEventListAdapter(val onBabyEventClickListener: OnBabyEventClickListener) : ListAdapter<BabyEvent, BabyEventListAdapter.BabyEventViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyEventViewHolder {
        return BabyEventViewHolder(BabyEventListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BabyEventViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onBabyEventClickListener.onClick(item)
        }
        holder.onBind(item)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<BabyEvent>() {
        override fun areItemsTheSame(oldItem: BabyEvent, newItem: BabyEvent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: BabyEvent, newItem: BabyEvent): Boolean {
            return oldItem.startTime == newItem.startTime
        }
    }

    class OnBabyEventClickListener(val clickListener : (babyEvent: BabyEvent) -> Unit) {
        fun onClick(event: BabyEvent) = clickListener(event)
    }

    class BabyEventViewHolder(val binding: BabyEventListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(event: BabyEvent) {
            when(event.babyEvent.eventType) {
                  EventType.FEED -> bindFeedEvent(event)
//                EventType.DIAPER -> TODO()
//                EventType.SLEEP -> TODO()
//                EventType.BATH -> TODO()
//                EventType.PILLS -> TODO()
            }
        }

        private fun bindFeedEvent(event: BabyEvent) {
            binding.eventImage.setImageResource(transformFeedingTypeIntoImageResource(FeedingType.valueOf(event.subType!!)))
            var minutes = event.duration?.div(60000)
            var startDate = Date(event.startTime)
            var endDate = Date(event.endTime!!)
            //Make it work with possible translations
            var dayFormater = SimpleDateFormat("dd/MM HH:mm")
            var timeFormater = SimpleDateFormat("HH:mm")
            binding.eventMainDescription.text = "${event.profile} eated ${minutes} min."
            binding.eventSecondaryDescription.text = "From ${dayFormater.format(startDate)} till ${timeFormater.format(endDate)}"
        }
    }
}