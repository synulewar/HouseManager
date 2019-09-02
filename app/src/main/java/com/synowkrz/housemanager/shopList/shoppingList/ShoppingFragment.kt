package com.synowkrz.housemanager.shopList.shoppingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.ShoppingFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.ShoppingListAdapter

class ShoppingFragment : Fragment() {

    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: ShoppingFragmentBinding
    private lateinit var listName : String
    private lateinit var sortString : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        listName = ShoppingFragmentArgs.fromBundle(arguments!!).listName
        sortString = ShoppingFragmentArgs.fromBundle(arguments!!).sortString
        binding = ShoppingFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        viewModel = ViewModelProviders.of(this, ShoppingViewModel.Factory(activity!!.application, listName, sortString)).get(ShoppingViewModel::class.java)
        binding.viewModel = viewModel
        binding.mainShoppingList.adapter = ShoppingListAdapter(ShoppingListAdapter.OnShopItemClickListener {shopItem, view ->
            if (view.id == R.id.check_box) {
                viewModel.onItemBought(shopItem)
            }
        }, ShoppingListAdapter.OnShopItemLongClickListener {
            shopItem, view -> true
        })

        viewModel.onAddProductPressed.observe(this, Observer {
            if (it) {
                findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToAddItemsFragment(listName))
                viewModel.onAddProductFinished()
            }
        })

        viewModel.onGoToInactive.observe(this, Observer {
            if(it) {
                viewModel.onGotoInactiveFinished()
                findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToInactiveShoppingList(listName, sortString))
            }
        })

        return binding.root
    }
}
