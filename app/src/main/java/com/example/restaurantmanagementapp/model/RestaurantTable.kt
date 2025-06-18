package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantTable (
    val table_id: Long,
    val name: String? = null,
    val chair_number: Int? = null,
    val status: Boolean = false
)

