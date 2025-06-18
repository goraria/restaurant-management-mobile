package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class OrderItem(
    val productId: String? = null,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val imageUrl: String? = null
) {
    override fun toString(): String {
        return "OrderItem(productId='$productId', productName='$productName', productPrice=$productPrice, quantity=$quantity, imageUrl=$imageUrl)"
    }
}