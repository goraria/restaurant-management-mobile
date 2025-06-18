package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import at.favre.lib.crypto.bcrypt.BCrypt

object UserRepository {

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        val result = Database.client.from("users").select()
        result.decodeList<User>()
    }

    suspend fun getUserById(user_id: Int): User? = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("user_id", user_id) }
                }
                .decodeSingle<User>()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val hashedPassword = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
            val newUser = user.copy(password = hashedPassword)
            Database.client.from("users").insert(newUser)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUser(user: User, newPassword: String? = null): Boolean = withContext(Dispatchers.IO) {
        try {
            val updatedUser = if (!newPassword.isNullOrEmpty()) {
                val hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())
                user.copy(password = hashedPassword)
            } else {
                user
            }

            Database.client.from("users")
                .update(updatedUser) {
                    filter { eq("user_id", user.user_id) }
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

    suspend fun loginUser(email: String, password: String): User? = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("email", email) }
                }
                .decodeSingle<User>()

            val verifyResult = BCrypt.verifyer().verify(password.toCharArray(), result.password)
            if (verifyResult.verified) {
                result
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun isUsernameAvailable(username: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("username", username) }
                }
            val users = result.decodeList<User>()
            users.isEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
