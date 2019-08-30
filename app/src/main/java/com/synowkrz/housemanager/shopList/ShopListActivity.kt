package com.synowkrz.housemanager.shopList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.ActivityShopListBinding
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.*

class ShopListActivity : AppCompatActivity() {

    private lateinit var repository : HouseRepository
    private val activityJob = Job()
    private val activityScope = CoroutineScope(activityJob + Dispatchers.Main)

    lateinit var navController: NavController
    lateinit var binding: ActivityShopListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository  = HouseRepository(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_list)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_shop_list)
        NavigationUI.setupWithNavController(binding.navView, navController)
        setUpBottomNavigation()
        activityScope.launch {
            withContext(Dispatchers.IO) {
                initializeShopitems(repository, application)
            }
        }
    }

    private fun setUpBottomNavigation() {
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
               R.id.shop_lists -> navController.navigate(R.id.shopListFragment)
                R.id.shop_items -> navController.navigate(R.id.itemList)
                R.id.shops_paths -> navController.navigate(R.id.shopAreaList)
                R.id.shop_close -> finish()
            }
            true
        }
    }
}
