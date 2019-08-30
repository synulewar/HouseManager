package com.synowkrz.housemanager.shopList.shopAreaConfigurator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.databinding.ShopAreaConfiguratorFragmentBinding
import com.synowkrz.housemanager.shopList.adapters.AreaConfigurationAdapter

class ShopAreaConfigurator : Fragment() {

    private lateinit var viewModel: ShopAreaConfiguratorViewModel
    private lateinit var binding : ShopAreaConfiguratorFragmentBinding
    private lateinit var shopAreaName : String
    private lateinit var areaAdapter : AreaConfigurationAdapter
    private lateinit var pathAdapter : AreaConfigurationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = ShopAreaConfiguratorFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        shopAreaName = ShopAreaConfiguratorArgs.fromBundle(arguments!!).shopAreaName
        viewModel = ViewModelProviders.of(this, ShopAreaConfiguratorViewModel.Factory(activity!!.application, shopAreaName))
            .get(ShopAreaConfiguratorViewModel::class.java)


        areaAdapter = AreaConfigurationAdapter(activity!!.application, AreaConfigurationAdapter.OnConfigurationClickListener{
            viewModel.switchFromAreasToPath(it)

        })

        pathAdapter= AreaConfigurationAdapter(activity!!.application, AreaConfigurationAdapter.OnConfigurationClickListener{
            viewModel.switchFromPathToAreas(it)
        })

        binding.shopAreaList.adapter = areaAdapter


        binding.shopPathList.adapter = pathAdapter

        viewModel.shopPath.observe(this, Observer {
            pathAdapter.submitList(it)
            pathAdapter.notifyDataSetChanged()
        })

        viewModel.areasList.observe(this, Observer {
            areaAdapter.submitList(it)
            areaAdapter.notifyDataSetChanged()
        })


        binding.viewModel = viewModel
        return binding.root
    }
}
