package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class Category(
    val category_id: Long? = null,
    val category_name: String,
    val type: String,
//    val created_at: DateTime? = null,
//    val updated_at: OffsetDateTime? = null
) 