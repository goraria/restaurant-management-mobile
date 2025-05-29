package com.example.restaurantmanagementapp.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String? = null,
    val imageUrl: String? = null // Hoặc có thể dùng Int nếu là drawable resource
) 