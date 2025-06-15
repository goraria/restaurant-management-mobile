package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable

@Serializable
data class BillDetails (
    val bill_detail_id: Int,
    val bill_id: Int,
    val menu_id: Int
)