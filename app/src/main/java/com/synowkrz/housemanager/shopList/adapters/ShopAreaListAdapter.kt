package com.synowkrz.housemanager.shopList.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.ShopAreaListItemBinding
import com.synowkrz.housemanager.shopList.model.ShopArea

class ShopAreaListAdapter(var onShopAreaClickListener: OnShopAreaClickListener,
                          var onShopAreaLongClickListener: OnShopAreaLongClickListener)
    : ListAdapter<ShopArea, ShopAreaListAdapter.ShopAreaListViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopAreaListViewHolder {
        return ShopAreaListViewHolder(ShopAreaListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShopAreaListViewHolder, position: Int) {
        var item = getItem(position)
        Log.d(TAG, "${item.name}")
        holder.itemView.setOnClickListener {
            onShopAreaClickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onShopAreaLongClickListener.onClick(item)
        }
        holder.onBind(getItem(position))
    }

    class ShopAreaListViewHolder(val binding: ShopAreaListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(shopArea: ShopArea) {
            binding.property = shopArea
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ShopArea>() {
        override fun areItemsTheSame(oldItem: ShopArea, newItem: ShopArea): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ShopArea, newItem: ShopArea): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnShopAreaClickListener(val clicklistener: (shopArea: ShopArea) -> Unit) {
        fun onClick(shopArea: ShopArea) = clicklistener(shopArea)
    }


    class OnShopAreaLongClickListener(val clicklistener: (shopArea: ShopArea) -> Boolean) {
        fun onClick(shopArea: ShopArea) = clicklistener(shopArea)
    }

}