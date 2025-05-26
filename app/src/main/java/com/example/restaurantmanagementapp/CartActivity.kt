package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantmanagementapp.adapter.CartAdapter
import com.example.restaurantmanagementapp.databinding.CartJaptorActivityBinding
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.model.Product

class CartActivity : AppCompatActivity() {
    private lateinit var binding: CartJaptorActivityBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CartJaptorActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupCheckoutButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onItemClick = { cartItem ->
                // Navigate to product detail when item is clicked
                val intent = Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra("product_id", cartItem.productId)
                    putExtra("product_name", cartItem.productName)
                    putExtra("product_price", cartItem.productPrice)
                }
                startActivity(intent)
            },
            onQuantityChange = { cartItem, newQuantity ->
                // TODO: Update cart item quantity in your data source (e.g., ViewModel, Repository)
                updateTotalPrice()
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }

        // TODO: Load cart items from your data source
        // Ví dụ:
        val sampleCartItems = listOf(
            CartItem("1", "Cơm Tấm Sườn Bì Chả", 55000.0, 2, null),
            CartItem("3", "Bún Chả Hà Nội", 50000.0, 1, null)
        )
        cartAdapter.submitList(sampleCartItems)
        updateTotalPrice() // Gọi sau khi load data
        android.util.Log.d("CartActivity", "Sample cart items submitted: ${sampleCartItems.size}")
    }

    private fun setupCheckoutButton() {
        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this, DeliveryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTotalPrice() {
        // TODO: Calculate total price from cart items (lấy từ adapter hoặc data source)
        val items = cartAdapter.currentList
        val totalPrice = items.sumOf { it.productPrice * it.quantity }
        binding.tvTotalPrice.text = formatPrice(totalPrice)
    }

    private fun formatPrice(price: Double): String {
        return String.format("%,.0fđ", price)
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