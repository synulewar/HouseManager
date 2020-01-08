package com.synowkrz.housemanager.shopList.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.MainShopListItemBinding
import com.synowkrz.housemanager.shopList.model.ExtendedShopList

class ShopListAdapter(val onShopListClickListener: OnShopListClickListener,
                      val onShopListLongClickListener: OnShopListLongClickListener) : ListAdapter<ExtendedShopList, ShopListAdapter.ShopListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        return ShopListViewHolder(MainShopListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onShopListClickListener.onClick(item, it)
        }

        holder.binding.editItem.setOnClickListener {
            onShopListClickListener.onClick(item, it)
        }

        holder.itemView.setOnLongClickListener {
            onShopListLongClickListener.onClick(item)
        }

        holder.onBind(item)
    }


    class ShopListViewHolder(val binding: MainShopListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(list: ExtendedShopList) {
            binding.property = list
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ExtendedShopList>() {
        override fun areItemsTheSame(oldItem: ExtendedShopList, newItem: ExtendedShopList): Boolean {
            return oldItem.shopList.name == newItem.shopList.name
        }

        override fun areContentsTheSame(oldItem: ExtendedShopList, newItem: ExtendedShopList): Boolean {
            return oldItem.shopList.name == newItem.shopList.name && oldItem.itemNumber == newItem.itemNumber
        }
    }

    class OnShopListClickListener(val clickListener: (shopList: ExtendedShopList, view: View) -> Unit) {
        fun onClick(shopList: ExtendedShopList, view: View) = clickListener(shopList, view)
    }

    class OnShopListLongClickListener(val clickListener: (shopList: ExtendedShopList) -> Boolean) {
        fun onClick(shopList: ExtendedShopList) = clickListener(shopList)
    }
}