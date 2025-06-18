package com.example.restaurantmanagementapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.restaurantmanagementapp.model.User

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        with(prefs.edit()) {
            putLong("user_id", user.user_id)
            putString("email", user.email)
            putString("first_name", user.first_name)
            putString("last_name", user.last_name)
            putString("phone_number", user.phone_number)
            putBoolean("is_active", user.is_active)
            putBoolean("is_verified", user.is_verified)
            putString("profile_picture_url", user.profile_picture_url)
            putString("address", user.address)
            putLong("role", user.role)
            apply()
        }
    }

    fun getUser(): User? {
        val userId = prefs.getLong("user_id", -1)
        if (userId == -1L) {
            return null
        }

        return User(
            user_id = userId,
            email = prefs.getString("email", "") ?: "",
            first_name = prefs.getString("first_name", "") ?: "",
            last_name = prefs.getString("last_name", "") ?: "",
            phone_number = prefs.getString("phone_number", "") ?: "",
            password = "",
            is_active = prefs.getBoolean("is_active", true),
            is_verified = prefs.getBoolean("is_verified", false),
            profile_picture_url = prefs.getString("profile_picture_url", null),
            address = prefs.getString("address", null),
            last_login_at = null,
            created_at = kotlinx.datetime.Clock.System.now(),
            role = prefs.getLong("role", 0L),
            username = ""
        )
    }

    fun clearUser() {
        prefs.edit().clear().apply()
    }
}
