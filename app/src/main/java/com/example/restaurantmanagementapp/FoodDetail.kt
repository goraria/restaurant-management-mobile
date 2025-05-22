package com.example.restaurantmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FoodDetail : AppCompatActivity() {

    private lateinit var btnMinus: Button
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_detail)

        btnAdd = findViewById(R.id.btnAdd)
        btnMinus= findViewById(R.id.btnMinus)

        val name = intent.getStringExtra("foodName")
        val desc = intent.getStringExtra("foodDesc")
        val price = intent.getIntExtra("foodPrice", 0)
        val imageResId = intent.getIntExtra("foodImage", 0)

        findViewById<TextView>(R.id.tvfooddetail).text = name
        findViewById<TextView>(R.id.tvDescription).text = desc
        findViewById<TextView>(R.id.tvPrice).text = "$price đ"
        findViewById<ImageView>(R.id.ivFood).setImageResource(imageResId)

    }

}