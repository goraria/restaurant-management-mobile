package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class Category {
    var category_id: Long? = null
    var category_name: String? = null
    var type: String? = null
    var created_at: String? = null
    var updated_at: String? = null
}
