package com.example.restaurantmanagementapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.CartItem

import com.bumptech.glide.Glide
import com.example.restaurantmanagementapp.model.User
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodDetail : AppCompatActivity() {
    private lateinit var btnMinus: Button
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var btnOrder: Button
    private lateinit var tvQuantity: TextView

    private var tquantity = 1

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
        tvQuantity.text = tquantity.toString()

        // Lấy thông tin món ăn từ Intent
        val foodID = intent.getLongExtra("foodID", -1L)
        val foodName = intent.getStringExtra("foodName") ?: ""
        val foodDesc = intent.getStringExtra("foodDesc") ?: ""
        val foodPrice = intent.getDoubleExtra("foodPrice", 0.0)
        val imageUrl = intent.getStringExtra("foodImage")
        Log.d("MenuFragment", "Foods loaded: ${foodID}, $foodName, $foodDesc, $foodPrice, $imageUrl")
        Log.d("FoodDetail", "imageUrl: $imageUrl")
        // Đổ dữ liệu lên UI
        findViewById<TextView>(R.id.tvfooddetail).text = foodName
        findViewById<TextView>(R.id.tvDescription).text = foodDesc
        findViewById<TextView>(R.id.tvPrice).text = "${foodPrice} đ"
        val ivFood = findViewById<ImageView>(R.id.ivFood)
        if (!imageUrl.isNullOrBlank()) {
            // Nếu imageUrl là tên resource nội bộ (ví dụ: "canhga"), lấy resource id từ mipmap hoặc drawable
            val resId = resources.getIdentifier(imageUrl, "mipmap", packageName)
            if (resId != 0) {
                ivFood.setImageResource(resId)
            } else {
                val resIdDrawable = resources.getIdentifier(imageUrl, "drawable", packageName)
                if (resIdDrawable != 0) {
                    ivFood.setImageResource(resIdDrawable)
                } else {
                    ivFood.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }
        } else {
            ivFood.setImageResource(R.drawable.ic_launcher_foreground)
        }

        // Trả về tên món để debug
        Toast.makeText(this, "FoodDetail mở: $foodName", Toast.LENGTH_SHORT).show()

        // Sự kiện nút Back
        btnBack.setOnClickListener { finish() }

        // Tăng/giảm số lượng
        btnAdd.setOnClickListener {
            tquantity++
            tvQuantity.text = tquantity.toString()
        }
        btnMinus.setOnClickListener {
            if (tquantity > 1) {
                tquantity--
                tvQuantity.text = tquantity.toString()
            } else {
                Toast.makeText(this, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show()
            }
        }

        // Sự kiện Order: trả về kết quả cho CartFragment
        btnOrder.setOnClickListener {
            val quantityValue = tvQuantity.text.toString().toIntOrNull() ?: 1
            val tableId = 1L // hoặc lấy từ user/session
            lifecycleScope.launch {
                val cartItems = CartItemRepository.getCartItemByTableAndPaid(tableId, false)
                val existingCartItem = cartItems.find { it.menu_id == foodID }
                if (existingCartItem != null && existingCartItem.cart_item_id != null) {
                    // Nếu đã có, chỉ update quantity bằng hàm mới
                    val newQuantity = (existingCartItem.quantity ?: 0) + quantityValue
                    val success = CartItemRepository.updateCartItemQuantity(existingCartItem.cart_item_id!!, newQuantity)
                    if (success) {
                        Toast.makeText(this@FoodDetail, "Đã cập nhật số lượng trong giỏ hàng!", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@FoodDetail, "Cập nhật giỏ hàng thất bại!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val cartItem = CartItem().apply {
                        menu_id = foodID
                        table_id = tableId
                        quantity = quantityValue
                        paid = false
                    }
                    val success = CartItemRepository.addCartItem(cartItem)
                    if (success) {
                        Toast.makeText(this@FoodDetail, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@FoodDetail, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

