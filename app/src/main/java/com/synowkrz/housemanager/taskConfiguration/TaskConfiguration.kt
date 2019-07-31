package com.synowkrz.housemanager.taskConfiguration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.FragmentTaskConfigurationBinding

class TaskConfiguration : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentTaskConfigurationBinding.inflate(inflater)
        val taskType = TaskConfigurationArgs.fromBundle(arguments!!).taskType
        val activity = requireNotNull(this.activity)
        val viewModel = ViewModelProviders.of(this, TaskConfigurationViewModel.Factory(activity.application, taskType))
            .get(TaskConfigurationViewModel::class.java)
        binding.viewModel = viewModel
        binding.buttonOk.setOnClickListener{
            viewModel.onOkButtonClicked(binding.editText.text.toString())
        }


        viewModel.emptyString.observe(this, Observer {
            if (it) {
                viewModel.onEmptyEventCompleted()
                Snackbar.make(binding.root, activity.getString(R.string.error_string), Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.itemAdded.observe(this, Observer {
            if (it) {
                viewModel.onItemAddedCompleted()
                findNavController().navigate(TaskConfigurationDirections.actionTaskConfigurationToHomeMenu())
            }
        })

        viewModel.databaseError.observe(this, Observer {
            if (it) {
                viewModel.onDatabaseErrorCompleted()
                Snackbar.make(binding.root, activity.getString(R.string.error_string), Snackbar.LENGTH_LONG).show()
            }
        })
        return binding.root
    }
}
