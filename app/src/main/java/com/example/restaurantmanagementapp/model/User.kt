package com.example.restaurantmanagementapp.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class User(
    val user_id: Long? = null,
    val username: String,
    val email: String,
    val phone_number: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val profile_picture_url: String? = null,
    val address: String? = null,
    val is_active: Boolean = true,
    val is_verified: Boolean = false,
    val last_login_at: Instant? = null,
    val created_at: Instant = Instant.fromEpochMilliseconds(System.currentTimeMillis()),
    val role: Long
)