package com.example.appcontrole.ui.technicalsupport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TechnicalSupportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is technical Fragment"
    }
    val text: LiveData<String> = _text
}