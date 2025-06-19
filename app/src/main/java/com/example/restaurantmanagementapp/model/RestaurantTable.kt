package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantTable (
    var table_id: Long? = null,
    var name: String? = null,
    var chair_number: Int? = null,
    var status: Boolean? = null
)

