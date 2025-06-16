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
class Menu {
    var menu_id: Long? = null
    var name: String? = null
    var price: Double? = null
    var stock: Boolean? = null
    var created_at: String? = null
    var updated_at: String? = null
    var description: String? = null
    var image_url: String? = null
}
