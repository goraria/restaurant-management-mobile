package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurantmanagementapp.adapter.ProductAdapter
import com.example.restaurantmanagementapp.databinding.ProductJaptorActivityBinding
import com.example.restaurantmanagementapp.model.Product

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ProductJaptorActivityBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProductJaptorActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearchBar()
//        setupCartButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            // Handle product click - navigate to product detail
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.name)
                putExtra("product_price", product.price)
                putExtra("product_description", product.description)
                putExtra("product_imageUrl", product.imageUrl)
            }
            startActivity(intent)
        }

        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductActivity)
            adapter = productAdapter
        }

        // TODO: Load products from your data source
        // Ví dụ:
        val sampleProducts = listOf(
            Product("1", "Cơm Tấm Sườn Bì Chả", 55000.0, "Cơm tấm đặc biệt với sườn nướng, bì thái sợi, chả trứng hấp. Kèm đồ chua và nước mắm pha.", null),
            Product("2", "Phở Bò Tái Nạm Gầu", 65000.0, "Phở bò truyền thống với thịt tái, nạm, gầu. Nước dùng đậm đà, bánh phở mềm.", null),
            Product("3", "Bún Chả Hà Nội", 50000.0, "Bún chả với chả miếng, chả băm nướng than hoa. Ăn kèm rau sống và nước chấm chua ngọt.", null),
            Product("4", "Mì Quảng Ếch", 70000.0, "Mì quảng đặc sản với thịt ếch đồng xào thấm vị, bánh tráng nướng giòn.", null),
            Product("5", "Gỏi Cuốn Tôm Thịt", 45000.0, "Gỏi cuốn thanh đạm với tôm, thịt luộc, bún, rau sống. Chấm cùng tương đậu phộng.", null)
        )
        productAdapter.submitList(sampleProducts)
        android.util.Log.d("ProductActivity", "Sample products submitted: ${sampleProducts.size}")
    }

    private fun setupSearchBar() {
        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            // TODO: Implement search functionality
            true
        }
    }

//    private fun setupCartButton() {
//        binding.fabCart.setOnClickListener {
//            val intent = Intent(this, CartActivity::class.java)
//            startActivity(intent)
//        }
//    }

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