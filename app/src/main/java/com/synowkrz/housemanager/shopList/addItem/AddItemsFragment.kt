package com.synowkrz.housemanager.shopList.addItem

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.AddItemsFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.AddItemListAdapter
import com.synowkrz.housemanager.shopList.model.Category
import com.synowkrz.housemanager.shopList.model.Measurements

class AddItemsFragment : Fragment() {

    private lateinit var viewModel: AddItemsViewModel
    private lateinit var binding: AddItemsFragmentBinding
    private lateinit var listName: String
    private lateinit var adapter : AddItemListAdapter
    private var categoryPosition : Int? = null
    private var categoryName : String? = null
    private var currentSearchText : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        listName =  AddItemsFragmentArgs.fromBundle(arguments!!).listName
        binding = AddItemsFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        adapter = AddItemListAdapter(AddItemListAdapter.OnClickListener {shopItem, binding ->
            Toast.makeText(context, "Add ${shopItem.name} ${binding.amount.text}", Toast.LENGTH_SHORT).show()
            viewModel.onProductAdd(shopItem, binding.amount.text.toString())
        }, activity!!.application)
        binding.itemList.adapter = adapter
        viewModel = ViewModelProviders.of(this, AddItemsViewModel.Factory(activity!!.application, listName)).get(AddItemsViewModel::class.java)

        viewModel.onNewItemAdded.observe(this, Observer {
            if (it) {
                showNewitemDialog()
            }
        })

        viewModel.itemList.observe(this, Observer {
            Log.d(TAG, "Persistent data set changed!")
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        binding.categorySpinnerSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Nothing selected", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val stringArray = resources.getStringArray(R.array.category_list_with_all)
                Toast.makeText(context, stringArray[position], Toast.LENGTH_SHORT).show()
                categoryPosition = position
                binding.itemSearchView.setQuery("", false)
                viewModel.changeDataSource(getCategoryFromString(stringArray[position]))
            }

        }

        binding.itemSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange ${newText}")
                currentSearchText = newText
                newText?.let {
                    viewModel.changeDataSource(newText)
                }
                return true
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
            var category = getCategoryFromString(categoryView.selectedItem.toString())
            var measurement = when(measurmentView.selectedItem.toString()) {
                getString(R.string.weight) -> Measurements.WEIGHT
                getString(R.string.volume) -> Measurements.VOLUME
                getString(R.string.quantity) -> Measurements.QUANTITY
                else -> Measurements.QUANTITY
            }

            viewModel.addNewPersistentShopItem(nameView.text.toString(),
                amountView.text.toString(), category, measurement)
        }

        categoryPosition?.let {
            if (it != 0) {
                categoryView.setSelection(it - 1)
            }
        }
        builder.show()
        viewModel.onAddNewItemFinished()
    }


    fun getCategoryFromString(catString : String) : Category {
        return when(catString) {
            getString(R.string.bread_category) -> Category.BREAD
            getString(R.string.fruit_vegetables_category) -> Category.FRUIT_VEGETABLES
            getString(R.string.meat_category) -> Category.MEAT
            getString(R.string.dairy_category) -> Category.DAIRY
            getString(R.string.alcohol_category) -> Category.ALCOHOL
            getString(R.string.sweets_category) -> Category.SWEETS
            getString(R.string.snacks_category) -> Category.SNACKS
            getString(R.string.hygiene_category) -> Category.HYGIENE
            getString(R.string.drinkables_category) -> Category.DRINKABLES
            getString(R.string.can_preserves_category) -> Category.CAN_AND_PRESERVES
            getString(R.string.all_categories) -> Category.ALL
            else -> Category.OTHER
        }
    }
}
