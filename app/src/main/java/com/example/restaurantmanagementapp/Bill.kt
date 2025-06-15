package com.example.restaurantmanagementapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.repository.BillRepository
import kotlinx.coroutines.launch

class Bill : AppCompatActivity() {
    private lateinit var adapter: CartViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_detail)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMonAn)
        val txtTotal = findViewById<TextView>(R.id.tvcost)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lấy id bàn từ intent
        val tableId = intent.getIntExtra("tableId", -1)
        if (tableId == -1) {
            Toast.makeText(this, "Không xác định được bàn!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Lấy dữ liệu cart từ database cho bàn này
        lifecycleScope.launch {
            val cartItems = BillRepository.getCartByTableId(tableId)
            if (cartItems.isEmpty()) {
                Toast.makeText(this@Bill, "Không có món nào trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            // Chuyển sang list MonAn để đưa vào adapter
            val monAnList = cartItems.map {
                MonAn(
                    name = it.productName,
                    soLuong = it.quantity,
                    desc = "",
                    price = it.productPrice.toInt(),
                    imgid = R.drawable.ic_launcher_foreground // Sửa nếu bạn dùng imageUrl thực
                )
            }

            adapter = CartViewAdapter(monAnList)
            recyclerView.adapter = adapter

            // Tính tổng tiền và hiển thị
            val total = cartItems.sumOf { it.productPrice * it.quantity }
            txtTotal.text = "Tổng: ${"%,.0f".format(total)} đ"
        }
    }
}
