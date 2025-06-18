package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime

// @Serializable
// data class Menu (
//     val menu_id: Int,
//     val name: String,
//     @Contextual val price: BigDecimal,
//     val stock: Boolean,
//     @Contextual val created_at: OffsetDateTime,
//     @Contextual val updated_at: OffsetDateTime,
//     val description: String,
//     val image_url: String
// )

@Serializable
data class Menu(
    val menu_id: Long,
    val name: String? = null,
    val price: Double? = null,
    val stock: Boolean? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val description: String? = null,
    val image_url: String? = null
)