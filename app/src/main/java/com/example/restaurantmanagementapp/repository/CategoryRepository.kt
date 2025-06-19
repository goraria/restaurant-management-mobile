package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Category
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CategoryRepository {

    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            Database.client.from("category").select().decodeList<Category>()
        } catch (e: Exception) {
            println("Error fetching categories: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addCategory(category: Category): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("category").insert(category)
            true
        } catch (e: Exception) {
            println("Error adding category: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun updateCategory(category: Category): Boolean = withContext(Dispatchers.IO) {
        try {
            if (category.category_id == null) {
                println("Category ID is null, cannot update.")
                return@withContext false
            }

            Database.client.from("category").update(
                mapOf(
                    "category_name" to category.category_name,
                    "type" to category.type,
                    "updated_at" to category.updated_at
                )
            ) {
                filter { eq("category_id", category.category_id!!) }
            }
            true
        } catch (e: Exception) {
            println("Error updating category: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteCategory(categoryId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("category").delete {
                filter { eq("category_id", categoryId) }
            }
            true
        } catch (e: Exception) {
            println("Error deleting category: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
