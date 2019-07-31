package com.synowkrz.housemanager.homeMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.TaskGridAdpater
import com.synowkrz.housemanager.databinding.HomeMenuFragmentBinding

class HomeMenu : Fragment() {

    private val viewModel: HomeMenuViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, HomeMenuViewModel.Factory(activity.application))
            .get(HomeMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = HomeMenuFragmentBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel

        viewModel.newTaskPressed.observe(this, Observer {
            if (it) {
                Toast.makeText(activity, "Float pressed", Toast.LENGTH_SHORT).show()
                findNavController().navigate(HomeMenuDirections.actionHomeMenuToChooserMenu())
                viewModel.onAddNewTaskCompleted()
            }
        })

        binding.taskGrid.adapter =
            TaskGridAdpater(TaskGridAdpater.OnClickListener {
                Toast.makeText(activity, "onClick ${it.name}", Toast.LENGTH_LONG).show()
            },TaskGridAdpater.OnLongClickListener{
                Toast.makeText(activity, "onLongClick ${it.name}", Toast.LENGTH_LONG).show()
                true
            })

        return binding.root
    }
}
