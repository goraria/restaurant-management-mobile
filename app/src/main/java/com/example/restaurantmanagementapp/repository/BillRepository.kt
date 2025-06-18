package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Bill
import com.example.restaurantmanagementapp.model.CartItem
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BillRepository {

    // Lấy các món trong cart theo id bàn
//    suspend fun getCartByTableId(tableId: Int): List<CartItem> = withContext(Dispatchers.IO) {
//        try {
//            val raw = Database.client.from("cart_items").select {
//                filter { eq("table_id", tableId) }
//            }.decodeList<CartItem>()
//
//            raw.forEach {
//                println("productId=${it.productId} | productName=${it.productName} | qty=${it.quantity}")
//            }
//            raw
//        } catch (e: Exception) {
//            println("Error fetching cart: ${e.message}")
//            e.printStackTrace()
//            emptyList()
//        }
//    }
    suspend fun addBill(bill: Bill): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("bills").insert(bill)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
