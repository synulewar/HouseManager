package com.synowkrz.housemanager.babyTask.BabyManager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.databinding.FragmentBabyMangerBinding


class BabyManger : Fragment() {

    private val viewModel: BabyManagerViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, BabyManagerViewModel.Factory(activity.application))
            .get(BabyManagerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val binding = FragmentBabyMangerBinding.inflate(inflater)


        return binding.root
    }
}
