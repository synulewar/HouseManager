package com.synowkrz.housemanager.homeTaskList.homeTaskDetail

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.text.set
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.TAG
import com.synowkrz.housemanager.databinding.HomeTaskDetailFragmentBinding
import com.synowkrz.housemanager.homeTaskList.model.HomeTask
import com.synowkrz.housemanager.homeTaskList.model.Interval

class HomeTaskDetail : Fragment() {

    private lateinit var viewModel: HomeTaskDetailViewModel
    private lateinit var binding: HomeTaskDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val name = HomeTaskDetailArgs.fromBundle(arguments!!).name
        binding = HomeTaskDetailFragmentBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this, HomeTaskDetailViewModel.Factory(activity!!.application, name)).get(HomeTaskDetailViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.taskName.text = name
        binding.viewModel = viewModel
        binding.doneTaskList.adapter = DoneTaskAdpater()

        viewModel.deleteHomeTask.observe(this, Observer {
            Log.d(TAG, "Delete home observer triggered $it")
            it?.let {
                showDeleteConfirmationDialog(it)
            }
        })

        viewModel.editHomeTask.observe(this, Observer {
            Log.d(TAG, "Edit home observer triggered $it")
            it?.let {
                showEditTaskDialog(it)
            }
        })

        return binding.root
    }


    fun showEditTaskDialog(homeTask: HomeTask) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_new_home_task_title))
        val view = layoutInflater.inflate(R.layout.dialog_new_hometask_item, null)
        val newView = view.findViewById(R.id.new_name) as EditText
        val frequencyView = view.findViewById(R.id.frequency) as EditText
        val intervalView = view.findViewById(R.id.interval_spinner) as Spinner
        builder.setView(view)

        newView.setText(homeTask.name)
        frequencyView.setText(homeTask.frequency.toString())

        builder.setPositiveButton(android.R.string.ok) { i, dialog ->
            var frequency = frequencyView.text.toString().toInt()
            var interval = when(intervalView.selectedItem.toString()) {
                getString(R.string.days) -> Interval.DAYS
                getString(R.string.weeks) -> Interval.WEEKS
                getString(R.string.months) -> Interval.MONTHS
                getString(R.string.years) -> Interval.YEARS
                else -> Interval.DAYS
            }
            viewModel.updateHomeTask(homeTask, frequency, interval)
            findNavController().navigate(HomeTaskDetailDirections.actionHomeTaskDetailToHomeTaskMainFragment())
        }

        builder.setNegativeButton(android.R.string.cancel) {
                dialogInterface, i ->
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
        }

        builder.show()
        viewModel.onEditFinished()
    }


    fun showDeleteConfirmationDialog(homeTask : HomeTask) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.delete_confirmation))
        builder.setPositiveButton(android.R.string.ok) { i, dialog ->
            viewModel.removeHomeTask(homeTask)
            findNavController().navigate(HomeTaskDetailDirections.actionHomeTaskDetailToHomeTaskMainFragment())
        }

        builder.setNegativeButton(android.R.string.cancel) {i, d ->
            //do nothing
        }
        viewModel.onDeleteFinished()

        builder.show()
    }
}
