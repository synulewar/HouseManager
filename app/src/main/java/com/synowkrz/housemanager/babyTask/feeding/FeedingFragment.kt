package com.synowkrz.housemanager.babyTask.feeding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.databinding.FragmentFeedingBinding


class FeedingFragment : Fragment() {

    private val viewModel: FeedingViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, FeedingViewModel.Factory(activity.application))
            .get(FeedingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val binding = FragmentFeedingBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }


}
