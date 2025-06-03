package com.example.restaurantmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.FoodBillAdapter
import com.example.restaurantmanagementapp.MonAn
import com.example.restaurantmanagementapp.R


class Bill : AppCompatActivity() {

    private lateinit var adapter: FoodBillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bill_detail)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMonAn)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FoodBillAdapter(mutableListOf())
        recyclerView.adapter = adapter

        adapter.addMonAn(
            MonAn(
                name = "Cánh gà",
                soLuong = 6,
                desc= "Cánh gà chiên giòn, ngon tuyệt vời.",
                price = 666_666,
                imgid = R.mipmap.canhga
            )
        )
        adapter.addMonAn(
            MonAn(
                name = "Gà rán",
                soLuong = 3,
                desc= "Gà rán vàng ruộm, béo ngậy.",
                price = 299_000,
//                imgid = R.mipmap.chips_m
            )
        )

        val btnThanhToan = findViewById<Button>(R.id.btncheckout)
        btnThanhToan.setOnClickListener {
            Toast.makeText(this, "Thanh toán chưa làm :)", Toast.LENGTH_SHORT).show()
        }
    }
}
