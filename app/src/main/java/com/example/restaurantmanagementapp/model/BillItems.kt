package com.example.restaurantmanagementapp.model

data class BillItems(
    val bill: Bill,
    val items: List<CartItem>
)