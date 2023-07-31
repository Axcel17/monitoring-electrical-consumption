package com.example.appcontrole.ui.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is record Fragment"
    }
    val text: LiveData<String> = _text
}