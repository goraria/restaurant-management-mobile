package com.example.restaurantmanagementapp

data class MonAn(
    val name: String,
    var soLuong: Int,
    val desc: String,
    val price: Int,
    val imgid: Int = R.mipmap.ic_launcher
)
