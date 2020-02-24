package com.synowkrz.housemanager.babyTask.babyManager


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.LATEST_ACTIVE_PROFILE
import com.synowkrz.housemanager.SHARED_PREF
import com.synowkrz.housemanager.babyTask.BabyActivity
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.babyTask.model.EventType
import com.synowkrz.housemanager.databinding.FragmentBabyMangerBinding


class BabyManager : Fragment() {

    private lateinit var viewModel: BabyManagerViewModel
    private lateinit var name : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        name = BabyManagerArgs.fromBundle(arguments!!).babyProfile
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProviders.of(this, BabyManagerViewModel.Factory(activity.application, name))
            .get(BabyManagerViewModel::class.java)

        var sharedPref = activity.applicationContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        sharedPref.edit().putString(LATEST_ACTIVE_PROFILE, name).apply()

        val binding = FragmentBabyMangerBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.babyActionGrid.adapter = BabyActionGridAdapter(BabyActionGridAdapter.OnClickListener {
            if (it.eventType == EventType.FEED) {
                findNavController().navigate(BabyManagerDirections.actionBabyMangerToFeedingFragment(name))
            }
        })
        (activity as BabyActivity).supportActionBar?.title = "BabyManager"
        return binding.root
    }

}
