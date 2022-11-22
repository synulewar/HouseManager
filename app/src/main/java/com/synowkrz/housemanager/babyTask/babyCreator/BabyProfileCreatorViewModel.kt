package com.synowkrz.housemanager.babyTask.babyCreator

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.synowkrz.housemanager.babyTask.model.BabyProfile
import com.synowkrz.housemanager.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class BabyProfileCreatorViewModel(val app : Application) : AndroidViewModel(app) {

    companion object {
        val DATE_REGEX = "\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d".toRegex()
    }

    private val repository = HouseRepository(app)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var photo: Uri? = null
    private lateinit var currentProfileList : List<BabyProfile>
    init {
        viewModelScope.launch {
            currentProfileList = repository.getAllBabiesData()
        }
    }

    private val _photoClicked = MutableLiveData<Boolean>()
    val photoClicked : LiveData<Boolean>
        get() = _photoClicked

    fun onBabyPhotoClicked() {
        _photoClicked.value = true
    }

    fun onBabyPhotoClickedFinished() {
        _photoClicked.value = false
    }

    private val _dateClicked = MutableLiveData<Boolean>()
    val dateClicked : LiveData<Boolean>
        get() = _dateClicked

    fun onDateClicked() {
        _dateClicked.value = true
    }

    fun onDateClickedFinished() {
        _dateClicked.value = false
    }

    fun onPhotoDataReceived(photoUri: Uri?) {
        photo = photoUri
    }

    private val _okPressed = MutableLiveData<Boolean>()
    val okPressed : LiveData<Boolean>
        get() = _okPressed

    private val _cancelPressed = MutableLiveData<Boolean>()
    val cancelPressed : LiveData<Boolean>
        get() = _cancelPressed

    private val _emptyString = MutableLiveData<Boolean>()
    val emptyString : LiveData<Boolean>
        get() = _emptyString

    private val _nameNotUnique = MutableLiveData<Boolean>()
    val nameNotUnique : LiveData<Boolean>
        get() = _nameNotUnique

    private val _birthDateNotSet = MutableLiveData<Boolean>()
    val birthDateNotSet : LiveData<Boolean>
        get() = _birthDateNotSet

    private val _itemAdded = MutableLiveData<Boolean>()
    val itemAdded : LiveData<Boolean>
        get() = _itemAdded

    fun onOKPressed() {
        _okPressed.value = true
    }

    fun onCancelPressed() {
        _cancelPressed.value = true
    }

    fun onOkPressedFinished() {
        _okPressed.value = false
    }

    fun onCancelPressedFinished() {
        _cancelPressed.value = false
    }

    fun onEmptyStringFinshed() {
        _emptyString.value = false
    }

    fun onNameNotUniqueFinished() {
        _nameNotUnique.value = false
    }

    fun onItemAddedCompleted() {
        _itemAdded.value = false
    }

    fun onBirthDayNotSetCompleted() {
        _birthDateNotSet.value = false
    }


    fun addNewBabyProfile(name: String, birthDate: String) {
        if (name.isEmpty()) {
            _emptyString.value = true
            return
        }

        if (!DATE_REGEX.containsMatchIn(birthDate)) {
            _birthDateNotSet.value = true
            return
        }

        for (baby: BabyProfile in currentProfileList) {
            if (baby.name == name) {
                _nameNotUnique.value = true
                return
            }
        }

        val file = File(app.applicationContext.filesDir, "${name}.jpg")
        val photoUri = photo
        if (photoUri != null) {
            app.contentResolver.openInputStream(photoUri).use {input ->
                file.outputStream().use {
                    output -> input?.copyTo(output)
                }
            }
        }

        var babyPhoto = if (photoUri != null) file.absolutePath else "kid"
        viewModelScope.launch {
            repository.insertBabyProfile(BabyProfile(name, babyPhoto, birthDate))
            _itemAdded.value = true
        }

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BabyProfileCreatorViewModel(app) as T
        }
    }
}