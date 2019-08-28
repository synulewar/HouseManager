package com.synowkrz.housemanager.shopList.shopList

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        binding.mainShopList.adapter = ShopListAdapter(ShopListAdapter.OnShopListClickListener{
            findNavController().navigate(ShopListFragmentDirections.actionShopListFragmentToShoppingFragment(it.name))
        }, ShopListAdapter.OnShopListLongClickListener{
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
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialogInterface, i ->
            val newList = editText.text.toString()
            Toast.makeText(context, "New list name ${newList}", Toast.LENGTH_SHORT).show()
            viewModel.insertNewShopList(ShopList(newList))
        }

        builder.setNegativeButton(android.R.string.cancel) { dialogInterface, i ->
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }
}
