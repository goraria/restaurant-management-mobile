package com.example.restaurantmanagementapp.ui.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ManagementViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Management Fragment"
    }
    val text: LiveData<String> = _text
}