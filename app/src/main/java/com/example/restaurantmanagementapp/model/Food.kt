package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class Food {
    var menu_id: Long? = null
    var name: String? = null
    var price: Double? = null
    var stock: Boolean? = null
    var image_url: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var description: String? = null

    constructor()
    constructor(
        menu_id: Long?,
        name: String?,
        price: Double?,
        stock: Boolean?,
        image_url: String?,
        created_at: String?,
        updated_at: String?,
        description: String?
    ) {
        this.menu_id = menu_id
        this.name = name
        this.price = price
        this.stock = stock
        this.image_url = image_url
        this.created_at = created_at
        this.updated_at = updated_at
        this.description = description
    }

    override fun toString(): String {
        return "Food(menu_id=$menu_id, name=$name, price=$price, stock=$stock, image_url=$image_url, created_at=$created_at, updated_at=$updated_at, description=$description)"
    }


}