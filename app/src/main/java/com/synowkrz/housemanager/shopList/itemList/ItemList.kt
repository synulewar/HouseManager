package com.synowkrz.housemanager.shopList.itemList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.ItemListFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.AddItemListAdapter
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements

class ItemList : Fragment() {


    private lateinit var viewModel: ItemListViewModel
    private lateinit var binding: ItemListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = ItemListFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        viewModel = ViewModelProviders.of(this, ItemListViewModel.Factory(activity!!.application)).get(ItemListViewModel::class.java)
        Log.d(TAG, "Before adapter attach")
        binding.itemList.adapter = AddItemListAdapter(AddItemListAdapter.OnClickListener{ shopItem, binding ->
            Toast.makeText(context, "This will edit ${shopItem.name}", Toast.LENGTH_SHORT).show()
        }, false)

        viewModel.onAddPersistentItem.observe(this, Observer {
            if (it) {
                viewModel.onAddNewItemFinished()
                showNewitemDialog()
            }
        })
        binding.viewModel = viewModel
        return binding.root
    }


    fun showNewitemDialog () {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_new_product))
        val view = layoutInflater.inflate(R.layout.dialog_new_product, null)
        val nameView = view.findViewById(R.id.new_name) as EditText
        val amountView = view.findViewById(R.id.amount) as EditText
        val categoryView = view.findViewById(R.id.category_spinner) as Spinner
        val measurmentView = view.findViewById(R.id.mesurment_spinner) as Spinner
        builder.setView(view)

        builder.setNegativeButton(android.R.string.cancel) {
                dialogInterface, i ->
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
        }

        builder.setPositiveButton(android.R.string.ok) {
                dialogInterface, i ->
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
            var category = when(categoryView.selectedItem.toString()) {
                getString(R.string.bread_category) -> Category.BREAD
                getString(R.string.fruit_vegetables_category) -> Category.FRUIT_VEGETABLES
                getString(R.string.meat_category) -> Category.MEAT
                getString(R.string.dairy_category) -> Category.DAIRY
                getString(R.string.alcohol_category) -> Category.ALCOHOL
                getString(R.string.sweets) -> Category.SWEETS
                getString(R.string.snacks_category) -> Category.SNACKS
                getString(R.string.hygiene_category) -> Category.HYGIENE
                getString(R.string.drinkables_category) -> Category.DRINKABLES
                getString(R.string.can_preserves_category) -> Category.CAN_AND_PRESERVES
                else -> Category.OTHER
            }

            var measurement = when(measurmentView.selectedItem.toString()) {
                getString(R.string.weight) -> Measurements.WEIGHT
                getString(R.string.volume) -> Measurements.VOLUME
                getString(R.string.quantity) -> Measurements.QUANTITY
                else -> Measurements.QUANTITY
            }

            viewModel.addNewPersistentShopItem(nameView.text.toString(),
                amountView.text.toString(), category, measurement)
        }
        builder.show()
        viewModel.onAddNewItemFinished()
    }
}
