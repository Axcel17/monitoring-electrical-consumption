package com.example.appcontrole.ui.consultations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsultationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is consultation Fragment"
    }
    val text: LiveData<String> = _text
}