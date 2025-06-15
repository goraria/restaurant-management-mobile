package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    var quantity: Int,
    val imageUrl: String? = null
) 