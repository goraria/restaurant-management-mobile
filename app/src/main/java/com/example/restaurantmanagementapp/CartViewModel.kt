// CartViewModel.kt
package com.example.restaurantmanagementapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    // MutableLiveData chứa list các Food
    private val _items = MutableLiveData<MutableList<Food>>(mutableListOf())
    val items: LiveData<MutableList<Food>> = _items

    // Hàm thêm món
    fun addItem(food: Food) {
        // copy list hiện tại rồi thêm món mới
        val currentList = _items.value ?: mutableListOf()
        currentList.add(food)
        _items.value = currentList
    }

    // (tuỳ chọn) Xóa món hoặc clear toàn bộ:
    fun clear() {
        _items.value = mutableListOf()
    }
}
