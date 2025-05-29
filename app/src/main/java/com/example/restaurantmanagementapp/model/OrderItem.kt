package com.example.restaurantmanagementapp.model

// OrderItem có thể giống CartItem hoặc có thêm các thuộc tính khác nếu cần
data class OrderItem(
    val productId: String,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val imageUrl: String? = null
) 