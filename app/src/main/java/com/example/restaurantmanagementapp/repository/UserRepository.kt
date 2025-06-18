package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import at.favre.lib.crypto.bcrypt.BCrypt  // dùng thư viện bcrypt cho Android

object UserRepository {

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        val result = Database.client.from("users").select()
        result.decodeList<User>()
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

    suspend fun getUserIDByName(username: String): Long? = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("username", username) }
                }
                .decodeSingle<User>()
            result.user_id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("users")
                .update(user) {
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

    suspend fun loginUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("email", email) }
                }
                .decodeSingle<User>()

            val verifyResult = BCrypt.verifyer().verify(password.toCharArray(), result.password)
            return@withContext verifyResult.verified
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun registerUser(user: User): Boolean = withContext(Dispatchers.IO) {
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