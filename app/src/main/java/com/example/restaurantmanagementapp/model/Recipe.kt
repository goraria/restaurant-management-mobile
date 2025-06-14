package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class Recipe {
    var recipe_id: Long? = null
    var menu_id: Long? = null
    var ingredient_id: Long? = null
    var quantity: Double? = null
    var unit: String? = null
    var created_at: String? = null
    var updated_at: String? = null
}
