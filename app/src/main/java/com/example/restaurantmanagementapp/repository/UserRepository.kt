package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

object UserRepository {

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        val result = Database.client.from("users").select()
        result.decodeList<User>()
    }

    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
            val newUser = user.copy(password = hashedPassword)
            Database.client.from("users").insert(newUser)
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

//    suspend fun login(username: String, password: String): User? = withContext(Dispatchers.IO) {
//        try {
//            val result = Database.client.from("users")
//                .select {
//                    filter { eq("username", username) }
//                }
//            val users = result.decodeList<User>()
//            val user = users.firstOrNull()
//
//            if (user != null && BCrypt.checkpw(password, user.password)) {
//                user
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
    suspend fun loginUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = Database.client.from("users")
                .select {
                    filter { eq("email", email) }
                }
                .decodeSingle<User>()

            return@withContext BCrypt.checkpw(password, result.password)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    suspend fun registerUser(user: User): Boolean = withContext(Dispatchers.IO) {
        try {
            val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())
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
