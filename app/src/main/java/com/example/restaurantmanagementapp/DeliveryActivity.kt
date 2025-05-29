package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantmanagementapp.adapter.OrderAdapter
import com.example.restaurantmanagementapp.databinding.DeliveryActivityBinding
import com.example.restaurantmanagementapp.model.OrderItem

class DeliveryActivity : AppCompatActivity() {
    private lateinit var binding: DeliveryActivityBinding
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeliveryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupDeliveryInfo()
        setupPlaceOrderButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter { orderItem ->
            // Navigate to product detail when item is clicked
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product_id", orderItem.productId)
                putExtra("product_name", orderItem.productName)
                putExtra("product_price", orderItem.productPrice)
            }
            startActivity(intent)
        }

        binding.rvOrderItems.apply {
            layoutManager = LinearLayoutManager(this@DeliveryActivity)
            adapter = orderAdapter
        }

        // TODO: Load order items from your data source (e.g. from CartActivity or a ViewModel)
        // Ví dụ:
        val sampleOrderItems = listOf(
            OrderItem("1", "Cơm Tấm Sườn Bì Chả", 55000.0, 2, "@drawable/ic_placeholder_food_1"),
            OrderItem("3", "Bún Chả Hà Nội", 50000.0, 1, null)
        )
        orderAdapter.submitList(sampleOrderItems)
        updateOrderSummary() // Gọi sau khi load data
        android.util.Log.d("DeliveryActivity", "Sample order items submitted: ${sampleOrderItems.size}")
    }

    private fun setupDeliveryInfo() {
        // TODO: Load and display delivery address and phone number
        binding.tvDeliveryAddress.text = "123 Đường ABC, Phường XYZ, Quận 1, TP. HCM"
        binding.tvPhoneNumber.text = "0987654321"
        // updateOrderSummary() // Đã gọi ở setupRecyclerView sau khi có data
    }

    private fun setupPlaceOrderButton() {
        binding.btnPlaceOrder.setOnClickListener {
            // TODO: Implement order placement logic
            // After successful order placement, you might want to clear the cart
            // and navigate back to the main screen
            finish()
        }
    }

    // Thêm hàm tính tổng tiền cho DeliveryActivity nếu cần
    private fun updateOrderSummary() {
        val items = orderAdapter.currentList
        val subtotal = items.sumOf { it.productPrice * it.quantity }
        val deliveryFee = 15000.0 // Ví dụ phí ship
        // val total = subtotal + deliveryFee // Tính tổng cuối nếu có TextView riêng

        binding.tvTotalPrice.text = String.format("%,.0fđ", subtotal)
        binding.tvDeliveryFee.text = String.format("%,.0fđ", deliveryFee)
        // Ví dụ, nếu bạn có một TextView khác để hiển thị tổng cộng cuối cùng (bao gồm cả phí ship)
        // binding.tvFinalTotal.text = String.format("%,.0fđ", total)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 