package com.synowkrz.housemanager.shopList.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.MainShoppingListItemBinding
import com.synowkrz.housemanager.getAmountFormat
import com.synowkrz.housemanager.getMeasurmentString
import com.synowkrz.housemanager.shopList.model.ShopItem
import java.util.*

class ShoppingListAdapter(val onShopItemClickListener: OnShopItemClickListener,
                          val onShopItemLongClickListener: OnShopItemLongClickListener) : ListAdapter<ShopItem, ShoppingListAdapter.ShopItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ShopItemViewHolder {
        return ShopItemViewHolder(MainShoppingListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ShoppingListAdapter.ShopItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnClickListener {
            onShopItemClickListener.onClick(item, it)
        }

        holder.binding.checkBox.setOnClickListener {
            onShopItemClickListener.onClick(item, it)
        }

        holder.binding.itemName.setOnClickListener {
            onShopItemClickListener.onClick(item, it)
        }

        holder.itemView.setOnLongClickListener{
            onShopItemLongClickListener.onClick(item, it)
        }
        holder.onBind(item)
    }

    class ShopItemViewHolder(val binding: MainShoppingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(shopItem: ShopItem) {
            binding.itemName.text = shopItem.name
            binding.amount.text = String.format(getAmountFormat(shopItem.measurements), shopItem.amount, Locale.US)
            binding.measurment.text = getMeasurmentString(shopItem.measurements)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnShopItemClickListener(val clickListener: (shopItem: ShopItem, view: View) -> Unit) {
        fun onClick(shopItem: ShopItem, view: View) = clickListener(shopItem, view)
    }

    class OnShopItemLongClickListener(val clickListener: (shopItem: ShopItem, view: View) -> Boolean) {
        fun onClick(shopItem: ShopItem, view: View) = clickListener(shopItem, view)
    }
}