package com.synowkrz.housemanager.babyTask

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.synowkrz.housemanager.BABY_ARGUMENT_NAME
import com.synowkrz.housemanager.LATEST_ACTIVE_PROFILE
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.SHARED_PREF
import com.synowkrz.housemanager.databinding.ActivityBabyBinding

class BabyActivity : AppCompatActivity() {

    lateinit var binding: ActivityBabyBinding
    lateinit var navController: NavController
    private lateinit var latestActiveProfile : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_baby)
        setSupportActionBar(binding.toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_baby)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        navController.addOnDestinationChangedListener(NavigationListener(binding))
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.baby_manager -> {
                    val sharedPref = applicationContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
                    latestActiveProfile = sharedPref.getString(LATEST_ACTIVE_PROFILE, null) ?: ""
                    val bundle = bundleOf(BABY_ARGUMENT_NAME to latestActiveProfile)
                    navController.navigate(R.id.babyManger, bundle)
                }
                R.id.baby_profile -> navController.navigate(R.id.babyMainMenu)
                R.id.baby_close -> finish()
                else -> Toast.makeText(this, "else", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title != null) {
            Toast.makeText(this, "Selected " + item.title, Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class NavigationListener(val binding: ActivityBabyBinding): NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            when (destination.id) {
                R.id.babyProfileCreator,
                R.id.feedingFragment -> binding.bottomNavigation.visibility = View.INVISIBLE
                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }



}
