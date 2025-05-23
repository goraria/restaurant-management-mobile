package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class FoodDetail : AppCompatActivity() {

    private lateinit var btnMinus: Button
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var btnOrder: Button
    private lateinit var tvQuantity : TextView

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_detail)
        btnAdd = findViewById(R.id.btnAdd)
        btnMinus= findViewById(R.id.btnMinus)
        btnBack= findViewById(R.id.btnBack)
        btnOrder= findViewById(R.id.btnOrder)
        tvQuantity = findViewById(R.id.tvQuantity)

        tvQuantity.text = quantity.toString()

        val name = intent.getStringExtra("foodName")
        val desc = intent.getStringExtra("foodDesc")
        val price = intent.getIntExtra("foodPrice", 0)
        val imageResId = intent.getIntExtra("foodImage", 0)

        findViewById<TextView>(R.id.tvfooddetail).text = name
        findViewById<TextView>(R.id.tvDescription).text = desc
        findViewById<TextView>(R.id.tvPrice).text = "$price đ"
        findViewById<ImageView>(R.id.ivFood).setImageResource(imageResId)

        btnBack.setOnClickListener {
            finish()
        }

        btnOrder.setOnClickListener {
            // Lấy dữ liệu món ăn từ Intent
            val foodName = intent.getStringExtra("foodName") ?: return@setOnClickListener
            val foodDesc = intent.getStringExtra("foodDesc") ?: ""
            val foodPrice = intent.getIntExtra("foodPrice", 0)
            val foodImage = intent.getIntExtra("foodImage", R.mipmap.ic_launcher)

            // Tạo đối tượng MonAn
            val monAn = MonAn(
                name = foodName,
                soLuong = quantity,
                desc = foodDesc,
                price = foodPrice,
                imgid = foodImage
            )

            // Lưu vào ViewModel
            val viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
            viewModel.themMonAn(monAn)

            // Thông báo & quay lại
            Toast.makeText(this, "Đã thêm món: $foodName ($quantity)", Toast.LENGTH_SHORT).show()
            finish() // Quay về màn trước
        }

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


    }


}