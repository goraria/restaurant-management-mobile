package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.PostgrestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepository {
    private val client = SupabaseClient.client

    suspend fun getFoods(): Flow<List<Food>> = flow {
        try {
            val result = client.postgrest["foods"].select {
                columns(Columns.raw("*"))
            }
            val foods = result.decodeList<Food>()
            emit(foods)
        } catch (e: Exception) {
            // Log error
            e.printStackTrace()
            emit(emptyList())
        }
    }

    suspend fun getFoodById(id: String): Flow<Food?> = flow {
        try {
            val result = client.postgrest["foods"].select {
                columns(Columns.raw("*"))
                eq("id", id)
            }
            val food = result.decodeSingleOrNull<Food>()
            emit(food)
        } catch (e: Exception) {
            // Log error
            e.printStackTrace()
            emit(null)
        }
    }
} 