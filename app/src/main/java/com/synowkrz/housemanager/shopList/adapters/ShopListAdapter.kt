package com.synowkrz.housemanager.shopList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.MainShopListItemBinding
import com.synowkrz.housemanager.shopList.model.ShopList

class ShopListAdapter(val onShopListClickListener: OnShopListClickListener,
                      val onShopListLongClickListener: OnShopListLongClickListener) : ListAdapter<ShopList, ShopListAdapter.ShopListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        return ShopListViewHolder(MainShopListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onShopListClickListener.onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onShopListLongClickListener.onClick(item)
        }

        holder.onBind(item)
    }


    class ShopListViewHolder(val binding: MainShopListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(list: ShopList) {
            binding.property = list
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ShopList>() {
        override fun areItemsTheSame(oldItem: ShopList, newItem: ShopList): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ShopList, newItem: ShopList): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnShopListClickListener(val clickListener: (shopList: ShopList) -> Unit) {
        fun onClick(shopList: ShopList) = clickListener(shopList)
    }

    class OnShopListLongClickListener(val clickListener: (shopList: ShopList) -> Boolean) {
        fun onClick(shopList: ShopList) = clickListener(shopList)
    }
}