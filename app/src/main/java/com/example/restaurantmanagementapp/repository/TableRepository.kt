package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Table
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TableRepository {


    suspend fun getTables(): List<Table> = withContext(Dispatchers.IO) {
        try {
            Database.client.from("restaurant_table").select().decodeList<Table>()
        } catch (e: Exception) {
            println("Error fetching tables: ${'$'}{e.message}")
            e.printStackTrace()
            emptyList()
        }
        val raw = Database.client.from("restaurant_table").select().decodeList<Table>()
        println("=== DEBUG TABLES FROM DB ===")
        raw.forEach {
            println("table_id=${it.table_id} | name=${it.name} | status=${it.status} | chair=${it.chair_number}")
        }
        raw

    }


    suspend fun updateTable(table: Table): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client
                .from("restaurant_table")
                .update(mapOf("status" to table.status)) {
                    filter { eq("table_id", table.table_id) }
                }
            println("Updated table ${'$'}{table.table_id} with status ${'$'}{table.status}")
            true
        } catch (e: Exception) {
            println("Error updating table ${'$'}{table.table_id}: ${'$'}{e.message}")
            e.printStackTrace()
            false
        }
    }


}
