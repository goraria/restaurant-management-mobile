package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.CartItem
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BillRepository {

    // Lấy các món trong cart theo id bàn
    suspend fun getCartByTableId(tableId: Int): List<CartItem> = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items").select {
                filter { eq("table_id", tableId) }
            }.decodeList<CartItem>().also { cartItems ->
                cartItems.forEach {
                    println("cart_item_id=${it.cart_item_id} | menu_id=${it.menu_id} | quantity=${it.quantity}")
                }
            }
        } catch (e: Exception) {
            println("Error fetching cart: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}
