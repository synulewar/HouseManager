package com.synowkrz.housemanager

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.babyTask.babyMainMenu.BabyGridAdpater
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.model.TaskGridItem

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<TaskGridItem>?) {
    val adapter = recyclerView.adapter as TaskGridAdpater
    adapter.submitList(data)
}

@BindingAdapter("babyListData")
fun bindRecyclerViewBaby(recyclerView: RecyclerView, data: List<BabyProfile>?) {
    val adapter = recyclerView.adapter as BabyGridAdpater
    adapter.submitList(data)
}


@BindingAdapter("setImage")
fun bindImageView(imageView: ImageView, data: String) {
    var resource= when (data) {
        "kid" -> R.drawable.kid
        "calendar" -> R.drawable.calendar
        "shoplist" -> R.drawable.shoplist
        "tasklist" -> R.drawable.tasklist
        else -> R.drawable.defaultpic
    }
    imageView.setImageResource(resource)
}