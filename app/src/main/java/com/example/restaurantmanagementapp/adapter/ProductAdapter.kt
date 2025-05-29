package com.example.restaurantmanagementapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.databinding.ItemProductBinding
import com.example.restaurantmanagementapp.model.Product
import com.example.restaurantmanagementapp.R
// import com.bumptech.glide.Glide // Thêm thư viện Glide nếu bạn dùng URL cho ảnh

class ProductAdapter(private val onItemClicked: (Product) -> Unit) :
    ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onItemClicked(product)
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = String.format("%,.0fđ", product.price) // Định dạng giá
            binding.tvProductDescription.text = product.description ?: ""
            
            if (product.imageUrl != null) {
                // Nếu bạn dùng Glide cho URL thật:
                // Glide.with(binding.ivProductImage.context)
                //     .load(product.imageUrl)
                //     .placeholder(R.drawable.ic_placeholder_default) 
                //     .error(R.drawable.ic_placeholder_default)
                //     .into(binding.ivProductImage)
                // Hiện tại, nếu imageUrl là string tên drawable, cần chuyển đổi:
                val imageResId = binding.root.context.resources.getIdentifier(
                    product.imageUrl.substringAfterLast('/'), 
                    "drawable", 
                    binding.root.context.packageName
                )
                if (imageResId != 0) {
                    binding.ivProductImage.setImageResource(imageResId)
                } else {
                    binding.ivProductImage.setImageResource(R.drawable.ic_placeholder_default)
                }
            } else {
                binding.ivProductImage.setImageResource(R.drawable.ic_placeholder_default)
            }
            android.util.Log.d("ProductAdapter", "Binding product: ${product.name}")
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product):
            Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Product, newItem: Product):
            Boolean = oldItem == newItem
} 