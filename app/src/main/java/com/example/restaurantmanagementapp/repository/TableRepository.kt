package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.RestaurantTable
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TableRepository {

    suspend fun getTables(): List<RestaurantTable> = withContext(Dispatchers.IO) {
        val result = Database.client.from("tables").select()
        result.decodeList<RestaurantTable>()
    }

    suspend fun addTable(table: RestaurantTable): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("tables").insert(table)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateTable(table: RestaurantTable): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("tables").update(table) {
                filter { eq("table_id", table.table_id) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteTable(id: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("tables").delete {
                filter { eq("table_id", id) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
