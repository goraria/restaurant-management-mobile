package com.example.restaurantmanagementapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantmanagementapp.databinding.ActivityProductDetailBinding
import com.example.restaurantmanagementapp.R
// import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        val productId = intent.getStringExtra("product_id")
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getDoubleExtra("product_price", 0.0)
        val productDescription = intent.getStringExtra("product_description")
        val productImageUrl = intent.getStringExtra("product_imageUrl")

        binding.tvProductDetailName.text = productName ?: "N/A"
        binding.tvProductDetailPrice.text = String.format("%,.0fđ", productPrice)
        binding.tvProductDetailDescription.text = productDescription ?: "Không có mô tả."
        
        if (productImageUrl != null) {
            // Glide.with(this).load(productImageUrl).placeholder(R.drawable.ic_placeholder_default).into(binding.ivProductDetailImage)
            val imageResId = resources.getIdentifier(
                productImageUrl.substringAfterLast('/'), 
                "drawable", 
                packageName
            )
            if (imageResId != 0) {
                binding.ivProductDetailImage.setImageResource(imageResId)
            } else {
                binding.ivProductDetailImage.setImageResource(R.drawable.ic_placeholder_default)
            }
        } else {
            binding.ivProductDetailImage.setImageResource(R.drawable.ic_placeholder_default)
        }

        binding.btnAddToCart.setOnClickListener {
            // TODO: Implement add to cart logic
            // Ví dụ: CartRepository.addItem(Product(productId, productName, productPrice, ...))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarProductDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false) // Tiêu đề sẽ do TextView trong layout đảm nhiệm
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() // Cách mới để xử lý nút back
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 