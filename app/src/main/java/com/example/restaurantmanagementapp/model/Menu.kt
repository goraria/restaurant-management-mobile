package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

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
