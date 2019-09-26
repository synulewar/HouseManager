package com.synowkrz.housemanager.shopList.shopList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.ShopListFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.ShopListAdapter
import com.synowkrz.housemanager.shopList.model.ShopList

class ShopListFragment : Fragment() {

    private lateinit var viewModel: ShopListViewModel
    private lateinit var binding: ShopListFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = ShopListFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        viewModel = ViewModelProviders.of(this, ShopListViewModel.Factory(activity!!.application)).get(ShopListViewModel::class.java)
        viewModel.onAddNewList.observe(this, Observer {
            if (it) {
                showNewListDialog()
                viewModel.onAddNewListFinished()
            }
        })
        binding.mainShopList.adapter = ShopListAdapter(ShopListAdapter.OnShopListClickListener{ shopList, view ->
            if (view.id == R.id.edit_item) {
                showEditListDialog(shopList)
            } else {
                findNavController().navigate(ShopListFragmentDirections.actionShopListFragmentToShoppingFragment(shopList.name, viewModel.getSortString(shopList.shopName)))
            }

        }, ShopListAdapter.OnShopListLongClickListener{
            viewModel.removeShopList(it)
            true
        })
        binding.viewModel = viewModel
        return binding.root
    }

    fun showNewListDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.new_list_title))
        val view = layoutInflater.inflate(R.layout.dialog_new_list, null)
        val editText = view.findViewById(R.id.new_list_edit) as EditText
        val spinner = view.findViewById(R.id.areas_spinner) as Spinner
        viewModel.areaNames.observe( this, Observer {
            if (it !=  null) {
                val areasList = it
                var spinnerArray = Array(it.size) {
                    areasList[it]
                }
                val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, spinnerArray)
                spinner.adapter = adapter
            }
        }
        )
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialogInterface, i ->
            val newList = editText.text.toString()
            val shopArea = spinner.selectedItem.toString()
            Toast.makeText(context, "New list name ${newList} shop area ${shopArea}", Toast.LENGTH_SHORT).show()
            viewModel.insertNewShopList(ShopList(newList, shopArea))
        }

        builder.setNegativeButton(android.R.string.cancel) { dialogInterface, i ->
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }


    fun showEditListDialog(shopList: ShopList) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.edit_list))
        val view = layoutInflater.inflate(R.layout.dialog_new_list, null)
        val editText = view.findViewById(R.id.new_list_edit) as EditText
        val spinner = view.findViewById(R.id.areas_spinner) as Spinner
        viewModel.areaNames.observe( this, Observer {
            if (it !=  null) {
                val areasList = it
                var spinnerArray = Array(it.size) {
                    areasList[it]
                }
                val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, spinnerArray)
                spinner.adapter = adapter
            }
        }
        )
        editText.setText(shopList.name)
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialogInterface, i ->
            val newList = editText.text.toString()
            val shopArea = spinner.selectedItem.toString()
            viewModel.updateShopList(ShopList(newList, shopArea))
        }

        builder.setNegativeButton(android.R.string.cancel) { dialogInterface, i ->
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.show()

    }
}
