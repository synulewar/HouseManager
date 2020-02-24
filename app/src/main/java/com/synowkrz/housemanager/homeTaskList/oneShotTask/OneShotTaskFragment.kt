package com.synowkrz.housemanager.homeTaskList.oneShotTask

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.dagger.ViewModelFactory
import com.synowkrz.housemanager.databinding.DialogOneShotTaskAddBinding
import com.synowkrz.housemanager.databinding.OneShotTaskFragmentBinding
import com.synowkrz.housemanager.dejMnieBudyn
import com.synowkrz.housemanager.getDate
import com.synowkrz.housemanager.homeTaskList.model.OneCategory
import com.synowkrz.housemanager.homeTaskList.model.OneShotTask
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OneShotTaskFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    val oneViewModel: OneShotTaskViewModel by activityViewModels {viewModelFactory}
    lateinit var binding :  OneShotTaskFragmentBinding
    var categoryPosition : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OneShotTaskFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.oneTaskList.adapter = OneShotTaskAdapter(
            OneShotTaskAdapter.OnOneShotTaskListener {
                it.doTask()
                oneViewModel.updateTask(it)
        },
            OneShotTaskAdapter.OnOneShotTaskLongListener {
                showDeleteDialog(it)
                true
            }, activity!!.application)
        binding.viewModel = oneViewModel


        oneViewModel.newTask.observe(this, Observer {
            showNewTaskDialog()
        })

        binding.categorySpinnerSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Nothing selected", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val stringArray = resources.getStringArray(R.array.one_task_categor_with_all)
                Toast.makeText(context, stringArray[position], Toast.LENGTH_SHORT).show()
                categoryPosition = position
                oneViewModel.changeDataSource(stringArray[position])
            }

        }

        return binding.root
    }

    fun showDeleteDialog(oneShotTask: OneShotTask) {
        val dialog = AlertDialog.Builder(activity).apply {
            setTitle(R.string.delete_confirmation)
            setPositiveButton(android.R.string.ok) { d, i ->
                oneViewModel.deleteTask(oneShotTask)
            }
            setNegativeButton(android.R.string.cancel) {d, i ->
                d.dismiss()
            }
        }
        dialog.show()
    }

    fun showNewTaskDialog() {
        val dialog = AlertDialog.Builder(activity)
        val binding = DialogOneShotTaskAddBinding.inflate(layoutInflater)
        dialog.setView(binding.root)
        dialog.setPositiveButton(android.R.string.ok) { d, i ->
            val name = binding.name.text.toString()
            val date = binding.picker.getDate()
            val catString = binding.categorySpinner.selectedItem.toString()
            val category = oneViewModel.getCategoryFromString(catString)
            oneViewModel.insertNewTask(OneShotTask(name, date, category))
            d.dismiss()
        }
        dialog.setNegativeButton(android.R.string.cancel) { d, i -> d.cancel()}

        dialog.show()
    }



}
