package com.synowkrz.housemanager.homeTaskList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.ActivityHomeTaskListBinding
import com.synowkrz.housemanager.databinding.ActivityShopListBinding

class HomeTaskListActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var binding: ActivityHomeTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_task_list)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_home_task)
        NavigationUI.setupWithNavController(binding.navView, navController)
        setUpBottomNavigation()
    }


    private fun setUpBottomNavigation() {
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.task_list -> navController.navigate(R.id.homeTaskMainFragment)
                R.id.task_close -> finish()
            }
            true
        }
    }
}
