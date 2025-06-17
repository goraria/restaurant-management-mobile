package com.example.restaurantmanagementapp.repository

import android.util.Log
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FoodRepository{

    suspend fun getFoods(): List<User> = withContext(Dispatchers.IO) {
        val result = Database.client.from("menu").select()
        result.decodeList<User>()
    }

    suspend fun addFood(food: User): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("menu").insert(food)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getFoodById(menuId: Int): Food? = withContext(Dispatchers.IO) {
    val result = Database.client
        .from("menu")
        .select {
            filter {
                eq("menu_id", menuId)
            }
        }
        .decodeList<Food>()
    Log.d("FoodRepository", "Query result for menu_id=$menuId: $result")
    result.firstOrNull()


}
}