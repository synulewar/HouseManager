package com.synowkrz.housemanager.chooserMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.TaskGridAdpater
import com.synowkrz.housemanager.databinding.FragmentChooserMenuBinding

class ChooserMenu : Fragment() {

    private val viewModel: ChooserMenuViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, ChooserMenuViewModel.Factory(activity.application)).get(ChooserMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentChooserMenuBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.chooserGrid.adapter = TaskGridAdpater(TaskGridAdpater.OnClickListener{
            viewModel.onTaskChoosen(it)
        })

        viewModel.itemAdded.observe(this, Observer {
            if (it != null) {
                viewModel.onTaskChoosenCompleted()
                findNavController().navigate(ChooserMenuDirections.actionChooserMenuToTaskConfiguration(it))
            }
        })
        return binding.root
    }


}
