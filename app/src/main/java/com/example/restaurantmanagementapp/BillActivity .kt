package com.example.restaurantmanagementapp

import android.os.Bundle
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMonAn)
        val txtTotal = findViewById<TextView>(R.id.tvcost)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tableId = intent.getLongExtra("tableId", -1L)
        if (tableId == -1L) {
            Toast.makeText(this, "Không xác định được bàn!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val cartItems = BillRepository.getCartByTableId(tableId.toInt()) // chuyển về Int nếu method cần Int
            if (cartItems.isEmpty()) {
                Toast.makeText(this@BillActivity, "Không có món nào!", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val billList = mutableListOf<OrderItem>()

            cartItems.forEach { cartItem ->
                val food = cartItem.menu_id?.let { FoodRepository.getFoodById(it.toInt()) }
                food?.let {
                    billList.add(
                        OrderItem(
                            productId = cartItem.menu_id?.toString() ?: "",
                            productName = it.name ?: "",
                            productPrice = it.price ?: 0.0,
                            quantity = cartItem.quantity ?: 0,
                            imageUrl = it.image_url
                        )
                    )
                }
            }

            billAdapter = BillViewAdapter(billList)
            recyclerView.adapter = billAdapter

            val total = billList.sumOf { it.productPrice * it.quantity }
            txtTotal.text = "Tổng: ${"%,.0f".format(total)} đ"
        }
    }

}
