package com.example.restaurantmanagementapp.model

data class CartItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    var quantity: Int,
    val imageUrl: String? = null
) 