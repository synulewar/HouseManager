package com.synowkrz.housemanager.babyTask.babyCreator


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.databinding.FragmentBabyProfileCreatorBinding
import java.util.*


class BabyProfileCreator : Fragment() {

    companion object {
        private val READ_PERMISSION_CODE = 101
        private val IMAGE_PICK_CODE = 102
        private val NEED_RUNTIME_PERMISSION = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private val viewModel: BabyProfileCreatorViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, BabyProfileCreatorViewModel.Factory(activity.application))
            .get(BabyProfileCreatorViewModel::class.java)
    }

    private lateinit var binding: FragmentBabyProfileCreatorBinding
    var cal = Calendar.getInstance()

    private val datePickerLisener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
            binding.dateTextView.text = String.format("%02d.%02d.%d", day, month + 1, year) // In calendar month start with 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentBabyProfileCreatorBinding.inflate(inflater)
        binding.viewModel = viewModel
        viewModel.dateClicked.observe(this, Observer {
            if (it) {
                displayDatePicker()
                viewModel.onDateClickedFinished()
            }
        })


        viewModel.photoClicked.observe(this, Observer {
            if (it) {
                fetchImgFromGallery()
                viewModel.onBabyPhotoClickedFinished()
            }
        })

        viewModel.okPressed.observe(this, Observer {
            if (it) {
                viewModel.addNewBabyProfile(binding.babyName.text.toString(), binding.dateTextView.text.toString())
                viewModel.onOkPressedFinished()
            }
        })

        viewModel.cancelPressed.observe(this, Observer {
            if (it) {
                viewModel.onCancelPressedFinished()
                findNavController().navigate(BabyProfileCreatorDirections.actionBabyProfileCreatorToBabyMainMenu())
            }
        })

        viewModel.emptyString.observe(this, Observer {
            if (it) {
                Snackbar.make(binding.root,
                    activity!!.getString(R.string.empty_baby_name), Snackbar.LENGTH_LONG).show()
                viewModel.onEmptyStringFinshed()
            }
        })

        viewModel.nameNotUnique.observe(this, Observer {
            if (it) {
                Snackbar.make(binding.root,
                    activity!!.getString(R.string.empty_baby_name), Snackbar.LENGTH_LONG).show()
                viewModel.onNameNotUniqueFinished()
            }
        })

        viewModel.itemAdded.observe(this, Observer {
            if (it) {
                viewModel.onItemAddedCompleted()
                findNavController().navigate(BabyProfileCreatorDirections.actionBabyProfileCreatorToBabyMainMenu())
            }
        })

        viewModel.birthDateNotSet.observe(this, Observer {
            if (it) {
                Snackbar.make(binding.root,
                    activity!!.getString(R.string.birthday_not_set_warning), Snackbar.LENGTH_LONG).show()
                viewModel.onBirthDayNotSetCompleted()
            }
        })

        return binding.root
    }

    private fun fetchImgFromGallery() {
        if (NEED_RUNTIME_PERMISSION) {
            if (permissionNotGranted()) {
                checkPermission()
            } else {
                startFetchActivity()
            }
        } else {
            startFetchActivity()
        }
    }

    private fun startFetchActivity() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun checkPermission() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermissions(permissions, READ_PERMISSION_CODE);
    }

    private fun permissionNotGranted() : Boolean {
        return checkSelfPermission(
            activity!!.applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED
    }

    private fun displayDatePicker() {
        DatePickerDialog(context!!,
            datePickerLisener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            READ_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startFetchActivity()
                }
                else{
                    Snackbar.make(binding.root,
                        activity!!.getString(R.string.read_permission_denied), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            val uri = data?.data
            binding.babyPhoto.setImageURI(uri)
            viewModel.onPhotoDataReceived(uri)
        }
    }
}
