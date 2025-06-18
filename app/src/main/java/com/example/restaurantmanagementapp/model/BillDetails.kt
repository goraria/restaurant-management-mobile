package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class BillDetails (
    val bill_detail_id: Long? = null,
    val bill_id: Int,
    val cart_item_id: Long
)