package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class Ingredient {
    var ingredient_id: Long? = null
    var name: String? = null
    var price_ingredient: Double? = null
    var quantity: Double? = null
    var category_id: Long? = null
    var create_at: String? = null
    var update_at: String? = null
    var unit: String? = null

    constructor()

    constructor(
        ingredient_id: Long?,
        name: String?,
        price_ingredient: Double?,
        quantity: Double?,
        category_id: Long?,
        create_at: String?,
        update_at: String?,
        unit: String?
    ) {
        this.ingredient_id = ingredient_id
        this.name = name
        this.price_ingredient = price_ingredient
        this.quantity = quantity
        this.category_id = category_id
        this.create_at = create_at
        this.update_at = update_at
        this.unit = unit
    }
}