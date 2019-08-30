package com.synowkrz.housemanager.shopList.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.databinding.AddItemListItemBinding
import com.synowkrz.housemanager.getAmountFormat
import com.synowkrz.housemanager.getColorByCategory
import com.synowkrz.housemanager.getMeasurmentString
import com.synowkrz.housemanager.shopList.model.PersistentShopItem
import java.util.*

class AddItemListAdapter(val onClickListener: OnClickListener, val addToListAvialable : Boolean = true)
    : ListAdapter<PersistentShopItem, AddItemListAdapter.AddItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        return AddItemViewHolder(AddItemListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setBackgroundColor(getColorByCategory(item.category))

        if (addToListAvialable) {
            holder.binding.addProduct.setOnClickListener{
                onClickListener.onClick(item, holder.binding)
            }
        } else {
            holder.binding.addProduct.visibility = View.GONE
            holder.itemView.setOnClickListener {
                onClickListener.onClick(item, holder.binding)
            }
        }
        holder.onBind(item)
    }

    class AddItemViewHolder(val binding: AddItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(persistentShopItem: PersistentShopItem) {
            binding.itemName.text = persistentShopItem.name
            binding.amount.setText(String.format(getAmountFormat(persistentShopItem.measurements), persistentShopItem.defaultAmount, Locale.US))
            binding.measurment.text = getMeasurmentString(persistentShopItem.measurements)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<PersistentShopItem>() {
        override fun areItemsTheSame(oldItem: PersistentShopItem, newItem: PersistentShopItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PersistentShopItem, newItem: PersistentShopItem): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class OnClickListener(val clickListener: (shopItem: PersistentShopItem, binding: AddItemListItemBinding) -> Unit) {
        fun onClick(shopItem: PersistentShopItem, binding: AddItemListItemBinding) = clickListener(shopItem, binding)
    }
}