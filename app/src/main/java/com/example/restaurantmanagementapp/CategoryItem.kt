package com.example.restaurantmanagementapp

data class CategoryItem(
    val categoryName: String,
    val itemCount: Int,
    val status: String // "Đang hoạt động" hoặc "Tạm ẩn"
) 