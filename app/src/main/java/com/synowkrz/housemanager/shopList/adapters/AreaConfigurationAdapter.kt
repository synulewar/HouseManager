package com.synowkrz.housemanager.shopList.adapters

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.ConfigurationItemBinding
import com.synowkrz.housemanager.shopList.model.Category

class AreaConfigurationAdapter(val app : Application, val onClickListener: OnConfigurationClickListener) : ListAdapter<Category, AreaConfigurationAdapter.ConfigurationViewHolder>(DiffCallback)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationViewHolder {
        return ConfigurationViewHolder(ConfigurationItemBinding.inflate(LayoutInflater.from(parent.context)), app)
    }

    override fun onBindViewHolder(holder: ConfigurationViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.onBind(item)
    }


    companion object DiffCallback: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    class ConfigurationViewHolder(val binding: ConfigurationItemBinding, val app: Application) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(category: Category) {
            Log.d(TAG, "Bind ${category}")
            binding.category.text = convertCategoryToString(category)
        }

        fun convertCategoryToString(category: Category) : String {
            return when(category) {
                Category.BREAD -> app.getString(R.string.bread_category)
                Category.FRUIT_VEGETABLES -> app.getString(R.string.fruit_vegetables_category)
                Category.MEAT -> app.getString(R.string.meat_category)
                Category.DAIRY -> app.getString(R.string.dairy_category)
                Category.ALCOHOL -> app.getString(R.string.alcohol_category)
                Category.SWEETS -> app.getString(R.string.sweets)
                Category.SNACKS -> app.getString(R.string.snacks_category)
                Category.HYGIENE -> app.getString(R.string.hygiene_category)
                Category.DRINKABLES -> app.getString(R.string.drinkables_category)
                Category.OTHER -> app.getString(R.string.other_category)
            }
        }
    }

    class OnConfigurationClickListener(val clickListaner: (category : Category) -> Unit) {
        fun onClick(category: Category) = clickListaner(category)
    }
}