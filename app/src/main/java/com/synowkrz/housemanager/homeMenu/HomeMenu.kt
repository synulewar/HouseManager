package com.synowkrz.housemanager.homeMenu

import android.content.Intent
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
import com.synowkrz.housemanager.babyTask.BabyActivity
import com.synowkrz.housemanager.databinding.HomeMenuFragmentBinding
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes

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
              //TODO("Change it to mach mvvm model")
                startTaskActivity(it)
            },TaskGridAdpater.OnLongClickListener{
                viewModel.onItemLongPress(it)
                true
            })

        return binding.root
    }

    fun startTaskActivity(taskGridItem: TaskGridItem) {
        when(taskGridItem.taskType) {
            TaskTypes.BABY -> {
                val intent = Intent(activity!!.applicationContext, BabyActivity::class.java)
                startActivity(intent)
            }
            TaskTypes.CALENDAR -> TODO()
            TaskTypes.SHOP_LIST -> TODO()
            TaskTypes.TASK_LIST -> TODO()
            TaskTypes.CUSTOM -> TODO()
        }
    }
}
