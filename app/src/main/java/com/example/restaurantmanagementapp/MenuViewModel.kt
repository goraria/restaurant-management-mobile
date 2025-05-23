package com.example.restaurantmanagementapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val danhSachMonAn = MutableLiveData<MutableList<MonAn>>(mutableListOf())

    fun themMonAn(mon: MonAn) {
        val currentList = danhSachMonAn.value ?: mutableListOf()
        currentList.add(mon)
        danhSachMonAn.value = currentList
    }
}