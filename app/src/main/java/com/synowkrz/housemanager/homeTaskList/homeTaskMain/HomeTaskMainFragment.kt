package com.synowkrz.housemanager.homeTaskList.homeTaskMain

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.ActivityHomeTaskListBinding
import com.synowkrz.housemanager.databinding.HomeMenuFragmentBinding
import com.synowkrz.housemanager.databinding.HomeTaskMainFragmentBinding
import com.synowkrz.housemanager.homeTaskList.model.Interval

class HomeTaskMainFragment : Fragment() {

    private lateinit var viewModel: HomeTaskMainViewModel
    private lateinit var binding: HomeTaskMainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeTaskMainFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        viewModel = ViewModelProviders.of(this, HomeTaskMainViewModel.Factory(activity!!.application)).get(HomeTaskMainViewModel::class.java)

        binding.mainTaskList.adapter = HomeTaskMainAdapter(HomeTaskMainAdapter.OnHomeTaskClickListener {
            findNavController().navigate(HomeTaskMainFragmentDirections.actionHomeTaskMainFragmentToHomeTaskDetail(it.name))
        }, activity!!.application)

        viewModel.onAddTaskPressed.observe(this, Observer {
            if (it) {
                openNewTaskDialog()
            }
        })

        binding.viewModel = viewModel
        return binding.root
    }

    fun openNewTaskDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_new_home_task_title))
        val view = layoutInflater.inflate(R.layout.dialog_new_hometask_item, null)
        val newView = view.findViewById(R.id.new_name) as EditText
        val frequencyView = view.findViewById(R.id.frequency) as EditText
        val intervalView = view.findViewById(R.id.interval_spinner) as Spinner
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { i, dialog ->
            var name = newView.text.toString()
            var frequency = frequencyView.text.toString().toInt()
            var interval = when(intervalView.selectedItem.toString()) {
                getString(R.string.days) -> Interval.DAYS
                getString(R.string.weeks) -> Interval.WEEKS
                getString(R.string.months) -> Interval.MONTHS
                getString(R.string.years) -> Interval.YEARS
                else -> Interval.DAYS
            }

            viewModel.insertNewHomeTask(name, frequency, interval)
        }

        builder.setNegativeButton(android.R.string.cancel) {
                dialogInterface, i ->
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
        }

        builder.show()
        viewModel.onAddTaskPressedFinished()
    }



}
