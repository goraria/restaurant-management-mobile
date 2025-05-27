package com.example.restaurantmanagementapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FoodDetail : AppCompatActivity() {
    private lateinit var btnMinus: Button
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var btnOrder: Button
    private lateinit var tvQuantity: TextView

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_detail)

        // Gán view
        btnAdd = findViewById(R.id.btnAdd)
        btnMinus = findViewById(R.id.btnMinus)
        btnBack = findViewById(R.id.btnBack)
        btnOrder = findViewById(R.id.btnOrder)
        tvQuantity = findViewById(R.id.tvQuantity)

        // Khởi tạo giá trị ban đầu
        tvQuantity.text = quantity.toString()

        // Lấy thông tin món ăn từ Intent
        val foodName = intent.getStringExtra("foodName") ?: ""
        val foodDesc = intent.getStringExtra("foodDesc") ?: ""
        val foodPrice = intent.getIntExtra("foodPrice", 0)
        val imageResId = intent.getIntExtra("foodImage", 0)

        // Đổ dữ liệu lên UI
        findViewById<TextView>(R.id.tvfooddetail).text = foodName
        findViewById<TextView>(R.id.tvDescription).text = foodDesc
        findViewById<TextView>(R.id.tvPrice).text = "${foodPrice} đ"
        findViewById<ImageView>(R.id.ivFood).setImageResource(imageResId)

        // Trả về tên món để debug
        Toast.makeText(this, "FoodDetail mở: $foodName", Toast.LENGTH_SHORT).show()

        // Sự kiện nút Back
        btnBack.setOnClickListener { finish() }

        // Tăng/giảm số lượng
        btnAdd.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
        }
        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
            } else {
                Toast.makeText(this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show()
            }
        }

        // Sự kiện Order: trả về kết quả cho CartFragment
        btnOrder.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("name", foodName)
                putExtra("desc", foodDesc)
                putExtra("price", foodPrice)
                putExtra("quantity", quantity)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Đã thêm $foodName x$quantity", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
