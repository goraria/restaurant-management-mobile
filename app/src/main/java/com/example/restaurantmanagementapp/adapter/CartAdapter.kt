package com.example.restaurantmanagementapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.databinding.ItemCartBinding
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.R
// import com.bumptech.glide.Glide

class CartAdapter(
    private val onItemClick: (CartItem) -> Unit,
    private val onQuantityChange: (CartItem, Int) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.tvCartItemName.text = cartItem.productName
            binding.tvCartItemPrice.text = String.format("%,.0fđ", cartItem.productPrice * cartItem.quantity)
            binding.tvQuantity.text = cartItem.quantity.toString()

            if (cartItem.imageUrl != null) {
                // Glide.with(binding.ivCartItemImage.context).load(cartItem.imageUrl).placeholder(R.drawable.ic_placeholder_default).into(binding.ivCartItemImage)
                val imageResId = binding.root.context.resources.getIdentifier(
                    cartItem.imageUrl.substringAfterLast('/'), 
                    "drawable", 
                    binding.root.context.packageName
                )
                if (imageResId != 0) {
                    binding.ivCartItemImage.setImageResource(imageResId)
                } else {
                    binding.ivCartItemImage.setImageResource(R.drawable.ic_placeholder_default)
                }
            } else {
                binding.ivCartItemImage.setImageResource(R.drawable.ic_placeholder_default)
            }

            binding.root.setOnClickListener {
                onItemClick(cartItem)
            }

            binding.btnIncreaseQuantity.setOnClickListener {
                val newQuantity = cartItem.quantity + 1
                onQuantityChange(cartItem, newQuantity)
            }

            binding.btnDecreaseQuantity.setOnClickListener {
                if (cartItem.quantity > 1) {
                    val newQuantity = cartItem.quantity - 1
                    onQuantityChange(cartItem, newQuantity)
                } else {
                    // Optionally, implement item removal if quantity becomes 0
                    // onQuantityChange(cartItem, 0) 
                }
            }

            android.util.Log.d("CartAdapter", "Binding cart item: ${cartItem.productName}")
        }
    }
}

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem):
            Boolean = oldItem.productId == newItem.productId

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem):
            Boolean = oldItem == newItem
} 