package com.example.restaurantmanagementapp.repository

import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Bill
import com.example.restaurantmanagementapp.model.CartItem
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BillRepository {

    suspend fun addBill(bill: Bill): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("bills").insert(bill)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getBillsByTableIdAndPaymentStatus(
        tableId: Long,
        paymentStatus: Boolean
    ): List<Bill> = withContext(Dispatchers.IO) {
        val result = Database.client.from("bills")
            .select {
                filter {
                    eq("table_id", tableId)
                    eq("payment_status", paymentStatus)
                }
            }
            .decodeList<Bill>()
        result
    }

}
