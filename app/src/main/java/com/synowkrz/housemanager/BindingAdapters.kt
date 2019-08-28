package com.synowkrz.housemanager

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.babyTask.BabyEventListAdapter
import com.synowkrz.housemanager.babyTask.babyMainMenu.BabyGridAdpater
import com.synowkrz.housemanager.babyTask.babyManager.BabyActionGridAdapter
import com.synowkrz.housemanager.babyTask.model.BabyEvent
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.BasicBabyEvent
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.shopList.adapters.AddItemListAdapter
import com.synowkrz.housemanager.shopList.adapters.ShopListAdapter
import com.synowkrz.housemanager.shopList.adapters.ShoppingListAdapter
import com.synowkrz.housemanager.shopList.model.PersistentShopItem
import com.synowkrz.housemanager.shopList.model.ShopItem
import com.synowkrz.housemanager.shopList.model.ShopList
import java.io.File

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<TaskGridItem>?) {
    val adapter = recyclerView.adapter as TaskGridAdpater
    adapter.submitList(data)
}


@BindingAdapter("persistentListData")
fun bindPersistentListRecyclerView(recyclerView: RecyclerView, data: List<PersistentShopItem>?) {
    val adapter = recyclerView.adapter as AddItemListAdapter
    adapter.submitList(data)
}


@BindingAdapter("shopListData")
fun bindShopListRecyclerView(recyclerView: RecyclerView, data: List<ShopList>?) {
    val adapter = recyclerView.adapter as ShopListAdapter
    adapter.submitList(data)
}

@BindingAdapter("shoppingListData")
fun bindShoppingItemList(recyclerView: RecyclerView, data: List<ShopItem>?) {
    val adapter = recyclerView.adapter as ShoppingListAdapter
    adapter.submitList(data)
}

@BindingAdapter("babyListData")
fun bindRecyclerViewBaby(recyclerView: RecyclerView, data: List<BabyProfile>?) {
    val adapter = recyclerView.adapter as BabyGridAdpater
    adapter.submitList(data)
}

@BindingAdapter("babyActionListData")
fun bindRecyclerViewBabyAction(recyclerView: RecyclerView, data: List<BasicBabyEvent>?) {
    val adapter = recyclerView.adapter as BabyActionGridAdapter
    adapter.submitList(data)
}


@BindingAdapter("babyEventListData")
fun bindRecyclerViewBabyEvent(recyclerView: RecyclerView, data: List<BabyEvent>?) {
    val adapter = recyclerView.adapter as BabyEventListAdapter
    adapter.submitList(data)
}


@BindingAdapter("setImage")
fun bindImageView(imageView: ImageView, data: String) {
    imageView.setImageResource(trasformStringIntoResource(data))
}

@BindingAdapter("setBabyPhoto")
fun bindImageViewBabyPhoto(imageView: ImageView, data: String) {
    if (data == "kid") {
        imageView.setImageResource(R.drawable.kid)
    } else {
        var photo = File(data)
        if (photo.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(data))
        }
    }
}


@BindingAdapter("setBabyPhoto")
fun bindImageViewBabyPhoto(imageView: ImageView, data: BabyProfile?) {
    if (data == null) {
        return
    }
    if (data.photo == "kid") {
        imageView.setImageResource(R.drawable.kid)
    } else {
        var photo = File(data.photo)
        if (photo.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(data.photo))
        }
    }
}

@BindingAdapter("setBabyName")
fun bindTextViewWithBabyName(textView: TextView, data: BabyProfile?) {
    if (data == null) {
        return
    }
    textView.text = data.name
}

fun trasformStringIntoResource(data: String) : Int {
    return when (data) {
        "kid" -> R.drawable.kid
        "calendar" -> R.drawable.calendar
        "shoplist" -> R.drawable.shoplist
        "tasklist" -> R.drawable.tasklist
        "bath" -> R.drawable.bath
        "feed" -> R.drawable.feed
        "sleep" -> R.drawable.sleep
        "diaper" -> R.drawable.diaper
        "pills" -> R.drawable.pills
        else -> R.drawable.defaultpic
    }
}

fun transformFeedingTypeIntoImageResource(feedingType: FeedingType) : Int {
    return when(feedingType) {
        FeedingType.LEFT -> R.drawable.left
        FeedingType.RIGHT -> R.drawable.right
        FeedingType.BOTTLE -> R.drawable.bottle
        FeedingType.SOLID -> R.drawable.solid
    }
}