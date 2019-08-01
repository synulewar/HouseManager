package com.synowkrz.housemanager.babyTask.babyMainMenu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.databinding.FragmentBabyMainMenuBinding

class BabyMainMenu : Fragment() {

    private val viewModel: BabyMainMenuViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, BabyMainMenuViewModel.Factory(activity.application))
            .get(BabyMainMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentBabyMainMenuBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.babyGrid.adapter = BabyGridAdpater(BabyGridAdpater.OnClickListener{
            Toast.makeText(activity, "${it.name} clciked", Toast.LENGTH_LONG).show()
        }, BabyGridAdpater.OnLongClickListener {
            Toast.makeText(activity, "${it.name} clciked", Toast.LENGTH_LONG).show()
            true
        })

        viewModel.addBaby.observe(this, Observer {
            if (it) {
                findNavController().navigate(BabyMainMenuDirections.actionBabyMainMenuToBabyProfileCreator())
                viewModel.onAddBabyFinished()
            }
        })

        return binding.root
    }
}
