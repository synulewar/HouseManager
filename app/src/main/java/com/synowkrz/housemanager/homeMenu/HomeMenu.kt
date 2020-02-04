package com.synowkrz.housemanager.homeMenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.TaskGridAdpater
import com.synowkrz.housemanager.babyTask.BabyActivity
import com.synowkrz.housemanager.dagger.ViewModelFactory
import com.synowkrz.housemanager.databinding.HomeMenuFragmentBinding
import com.synowkrz.housemanager.homeTaskList.HomeTaskListActivity
import com.synowkrz.housemanager.model.TaskGridItem
import com.synowkrz.housemanager.model.TaskTypes
import com.synowkrz.housemanager.shopList.ShopListActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeMenu : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private val viewModel: HomeMenuViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(HomeMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding = HomeMenuFragmentBinding.inflate(inflater)

        binding.setLifecycleOwner(this)
        Log.d(TAG, "Before creating injected viewModel")
        binding.viewModel = viewModel
        Log.d(TAG, "View model injected")

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
            TaskTypes.SHOP_LIST -> {
                val intent = Intent(activity!!.applicationContext, ShopListActivity::class.java)
                startActivity(intent)
            }
            TaskTypes.TASK_LIST -> {
                val intent = Intent(activity!!.applicationContext, HomeTaskListActivity::class.java)
                startActivity(intent)
            }
            TaskTypes.CUSTOM -> TODO()
        }
    }
}
