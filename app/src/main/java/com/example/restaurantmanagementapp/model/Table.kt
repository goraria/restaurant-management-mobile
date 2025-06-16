package com.example.restaurantmanagementapp.model
import kotlinx.serialization.Serializable

@Serializable
data class Table(
    val table_id: Int,
    val name: String,
    val chair_number: Int,
    val status: Boolean
)
