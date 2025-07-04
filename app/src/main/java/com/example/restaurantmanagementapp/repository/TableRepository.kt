package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.RestaurantTable
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.JsonPrimitive

object TableRepository {

    suspend fun addTable(table: RestaurantTable): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("restaurant_table").insert(table)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun deleteTable(id: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("restaurant_table").delete {
                filter { eq("table_id", id) }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getTables(): List<RestaurantTable> = withContext(Dispatchers.IO) {
        try {
            val raw = Database.client.from("restaurant_table").select().decodeList<RestaurantTable>()
            raw.forEach {
                println("table_id=${it.table_id} | name=${it.name} | status=${it.status} | chair=${it.chair_number}")
            }
            raw
        } catch (e: Exception) {
            println("Error fetching tables: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateTable(table: RestaurantTable): Boolean = withContext(Dispatchers.IO) {
        try {
            val updateJson = buildJsonObject {
                put("name", table.name)
                put("chair_number", table.chair_number)
                put("status", table.status)
            }

            val response = Database.client
                .from("restaurant_table")
                .update(updateJson) {
                    filter { eq("table_id", table.table_id!!) }
                }

            println("Updated table ${table.table_id}: $response")
            true
        } catch (e: Exception) {
            println("Error updating table ${table.table_id}: ${e.message}")
            e.printStackTrace()
            false
        }
    }



    suspend fun updateTableStatus(tableId: Long, status: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client
                .from("restaurant_table")
                .update(mapOf("status" to status)) {
                    filter { eq("table_id", tableId) }
                }
            println("Updated table $tableId with status $status")
            true
        } catch (e: Exception) {
            println("Error updating table $tableId: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}
