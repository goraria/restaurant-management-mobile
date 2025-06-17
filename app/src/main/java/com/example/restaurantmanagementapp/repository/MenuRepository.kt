package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Menu
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MenuRepository {

    suspend fun getMenuItems(): List<Menu> = withContext(Dispatchers.IO) {
        val result = Database.client.from("menu").select()
        result.decodeList<Menu>()
    }

    suspend fun addMenuItem(item: Menu): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("menu").insert(item)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateMenuItem(item: Menu): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("menu").update(item) {
                filter { eq("menu_id", item.menu_id) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteMenuItem(id: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("menu").delete {
                filter { eq("menu_id", id) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
