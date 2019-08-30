package com.synowkrz.housemanager.shopList.shopArea

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
import com.synowkrz.housemanager.databinding.ShopaAreaListFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.ShopAreaListAdapter

class ShopAreaList : Fragment() {

    private lateinit var viewModel: ShopAreaListViewModel
    private lateinit var binding: ShopaAreaListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ShopaAreaListFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.shopAreaList.adapter = ShopAreaListAdapter(ShopAreaListAdapter.OnShopAreaClickListener{
           Toast.makeText(context, "${it.name} clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(ShopAreaListDirections.actionShopAreaListToShopAreaConfigurator(it.name))
        }, ShopAreaListAdapter.OnShopAreaLongClickListener{
            viewModel.deleteShopArea(it.name)
            true
        })

        viewModel = ViewModelProviders.of(this, ShopAreaListViewModel.Factory(activity!!.application))
            .get(ShopAreaListViewModel::class.java)

        viewModel.newShopArea.observe(this, Observer {
            if (it) {
                viewModel.onAddNewShopAreaFinished()
                showNewShopAreaDialog()
            }
        })
        binding.viewModel = viewModel
        return binding.root
    }


    fun showNewShopAreaDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_new_area))
        val view = layoutInflater.inflate(R.layout.new_shop_are_dialog, null)
        val areaName = view.findViewById(R.id.new_shop_area) as EditText
        builder.setView(view)

        builder.setNegativeButton(android.R.string.cancel) {
                dialogInterface, i ->
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
        }

        builder.setPositiveButton(android.R.string.ok) {
                dialogInterface, i ->
            Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
            viewModel.insertNewShopArea(areaName.text.toString())
        }
        builder.show()
    }

}
