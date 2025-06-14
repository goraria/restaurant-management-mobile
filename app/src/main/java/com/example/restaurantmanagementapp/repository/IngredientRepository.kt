package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Ingredient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object IngredientRepository {
    suspend fun getIngredients(): List<Ingredient> = withContext(Dispatchers.IO) {
        val result = Database.client.from("ingredients").select()
        result.decodeList<Ingredient>()
    }

    suspend fun addIngredient(ingredient: Ingredient): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("ingredients").insert(ingredient)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateIngredient(ingredient: Ingredient): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("ingredients")
                .update(ingredient) {
                    filter { eq("ingredient_id", ingredient.ingredient_id ?: -1) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteIngredient(ingredient_id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("ingredients")
                .delete {
                    filter { eq("ingredient_id", ingredient_id) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}