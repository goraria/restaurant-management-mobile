package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepository {

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        val result = Database.client.from("users").select()
        result.decodeList<User>()
    }

    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("users").insert(user)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("users")
                .update(user) {
                    filter { eq("user_id", user.user_id ?: -1) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteUser(user_id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("users")
                .delete {
                    filter { eq("user_id", user_id) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
