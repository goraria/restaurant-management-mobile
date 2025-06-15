package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
class CartItem{
    var cart_item_id: Long? = null
    var table_id: Long? = null
    var menu_id: Long? = null
    var quantity: Int? = null
    var added_at: String? = null
    var paid: Boolean? = null

    constructor()
    constructor(
        cart_item_id: Long?,
        table_id: Long?,
        menu_id: Long?,
        quantity: Int?,
        added_at: String?,
        paid : Boolean?
    ) {
        this.cart_item_id = cart_item_id
        this.table_id = table_id
        this.menu_id = menu_id
        this.quantity = quantity
        this.added_at = added_at
        this.paid = paid
    }
}
