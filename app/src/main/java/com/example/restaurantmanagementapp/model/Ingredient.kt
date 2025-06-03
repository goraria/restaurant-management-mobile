package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable
import java.util.Date

data class IngredientItem(
    var ingredient_id: Long? = null,
    var name: String? = null,
    var price_ingredient: Double? = null,
    var quantity: Double? = null,
    var category_id: Long? = null,
    var unit: String? = null,
    var create_at: Date? = null,
    var update_at: String? = null,
)

@Serializable
class Ingredient {
    var ingredient_id: Long? = null
    var name: String? = null
    var price_ingredient: Double? = null
    var quantity: Double? = null
    var category_id: Long? = null
    var unit: String? = null
    var create_at: String? = null
    var update_at: String? = null
//    var pricePerUnit: Double? = null
//    var isAvailable: Boolean = true

    constructor()

    constructor(
        ingredient_id: Long?,
        name: String?,
        price_ingredient: Double?,
        quantity: Double?,
        category_id: Long?,
        unit: String?,
        create_at: String?,
        update_at: String?
    ) {
        this.ingredient_id = ingredient_id
        this.name = name
        this.price_ingredient = price_ingredient
        this.quantity = quantity
        this.category_id = category_id
        this.unit = unit
        this.create_at = create_at
        this.update_at = update_at
    }

//    constructor(
//        ingredientId: Long?,
//        ingredientName: String?,
//        quantity: Double?,
//        unit: String?,
//        pricePerUnit: Double?,
//        isAvailable: Boolean
//    ) {
//        this.ingredientId = ingredientId
//        this.ingredientName = ingredientName
//        this.quantity = quantity
//        this.unit = unit
//        this.pricePerUnit = pricePerUnit
//        this.isAvailable = isAvailable
//    }
}