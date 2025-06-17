package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantTable (
    var table_id: Long,
    var name: String? = null,
    var chair_number: Int? = null,
    var status: Boolean? = null
){
    companion object {
        fun newWithoutId(name: String, chair: Int, status: Boolean): RestaurantTable {
            return RestaurantTable(0L, name, chair, status)
        }
    }
}

