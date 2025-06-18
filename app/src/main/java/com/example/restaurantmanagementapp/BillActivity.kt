package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.OrderItem
import com.example.restaurantmanagementapp.repository.BillRepository
import com.example.restaurantmanagementapp.repository.FoodRepository
import kotlinx.coroutines.launch

class BillActivity  : AppCompatActivity() {
    private lateinit var billAdapter: BillViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_detail)
        val btncheckout = findViewById<TextView>(R.id.btncheckout)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMonAn)
        val txtTotal = findViewById<TextView>(R.id.tvcost)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tableId = com.example.restaurantmanagementapp.util.TableSession.currentTableId
        if (tableId == -1L) {
            Toast.makeText(this, "Không xác định được bàn!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // Lấy bill mới nhất với trạng thái chưa thanh toán
            val bills = BillRepository.getBillsByTableIdAndPaymentStatus(tableId, false)
            val bill = bills.maxByOrNull { it.bill_id ?: 0L }
            val total = bill?.total_amount ?: 0.0
            txtTotal.text = "Tổng: ${"%,.0f".format(total)} $"

            val cartItems = CartItemRepository.getCartItemByTableAndPaid(tableId, false)
            val foods = CartItemRepository.getFoodsInCartByTableLoop(tableId)

            val billList = mutableListOf<OrderItem>()
            cartItems.forEach { cartItem ->
                val food = foods.find { it.menu_id == cartItem.menu_id }
                food?.let {
                    billList.add(
                        OrderItem(
                            productId = cartItem.menu_id?.toString(),
                            productName = it.name ?: "",
                            productPrice = it.price ?: 0.0,
                            quantity = cartItem.quantity ?: 0,
                            imageUrl = it.image_url
                        )
                    )
                }
            }
            Log.d("BillViewAdapter", "billList size = ${billList.size}----------------------")

            billAdapter = BillViewAdapter(billList)
            recyclerView.adapter = billAdapter
        }

        btncheckout.setOnClickListener {
            // Cập nhật paid=true cho tất cả cart item của bàn này (giữ nguyên table_id)
            lifecycleScope.launch {
                val cartItems = CartItemRepository.getCartItemByTableAndPaid(tableId, false)
                var allSuccess = true
                for (cartItem in cartItems) {
                    val cartItemId = cartItem.cart_item_id
                    if (cartItemId != null) {
                        val result = CartItemRepository.updateCartItemPaid(cartItemId, true)
                        if (!result) allSuccess = false
                    }
                }
                if (allSuccess) {
                    Toast.makeText(this@BillActivity, "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BillActivity, "Có lỗi khi cập nhật trạng thái thanh toán!", Toast.LENGTH_SHORT).show()
                }
                // Chuyển về MainActivity (không truyền navigateToHome, chỉ về màn chính)
                val intent = android.content.Intent(this@BillActivity, MainActivity::class.java)
                intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }

}