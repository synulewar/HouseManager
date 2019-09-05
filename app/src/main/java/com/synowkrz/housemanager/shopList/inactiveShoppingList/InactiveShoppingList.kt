package com.synowkrz.housemanager.shopList.inactiveShoppingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.InactiveShoppingListFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.ShoppingListAdapter
import com.synowkrz.housemanager.shopList.shoppingList.ShoppingFragmentArgs

class InactiveShoppingList : Fragment() {



    private lateinit var viewModel: InactiveShoppingListViewModel
    private lateinit var binding: InactiveShoppingListFragmentBinding
    private lateinit var listName : String
    private lateinit var sortString : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        listName = ShoppingFragmentArgs.fromBundle(arguments!!).listName
        sortString = ShoppingFragmentArgs.fromBundle(arguments!!).sortString
        viewModel = ViewModelProviders.of(this, InactiveShoppingListViewModel.Factory(activity!!.application, listName)).get(
            InactiveShoppingListViewModel::class.java)
        binding = InactiveShoppingListFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.inactiveShoppingList.adapter = ShoppingListAdapter(ShoppingListAdapter.OnShopItemClickListener { shopItem, view ->
            if (view.id == R.id.check_box) {
                viewModel.onActivated(shopItem)
            }
        }, ShoppingListAdapter.OnShopItemLongClickListener {
                shopItem, view -> true
        }, activity!!.application)

        viewModel.activItem.observe(this, Observer {
            if(it) {
                viewModel.onactiveItemFinished()
                findNavController().navigate(InactiveShoppingListDirections.actionInactiveShoppingListToShoppingFragment(listName, sortString))
            }

        })
        binding.viewModel = viewModel
        return binding.root
    }
}
