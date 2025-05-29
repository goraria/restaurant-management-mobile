package com.example.restaurantmanagementapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.databinding.ItemOrderBinding
import com.example.restaurantmanagementapp.model.OrderItem
import com.example.restaurantmanagementapp.R
// import com.bumptech.glide.Glide

class OrderAdapter(private val onItemClick: (OrderItem) -> Unit) :
    ListAdapter<OrderItem, OrderAdapter.OrderViewHolder>(OrderItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = getItem(position)
        holder.bind(orderItem)
        holder.itemView.setOnClickListener { 
            onItemClick(orderItem)
        }
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem) {
            binding.tvOrderItemName.text = orderItem.productName
            binding.tvOrderItemPrice.text = String.format("%,.0fđ", orderItem.productPrice * orderItem.quantity)
            binding.tvOrderItemQuantity.text = "SL: ${orderItem.quantity}"
            
            if (orderItem.imageUrl != null) {
                // Glide.with(binding.ivOrderItemImage.context).load(orderItem.imageUrl).placeholder(R.drawable.ic_placeholder_default).into(binding.ivOrderItemImage)
                val imageResId = binding.root.context.resources.getIdentifier(
                    orderItem.imageUrl.substringAfterLast('/'), 
                    "drawable", 
                    binding.root.context.packageName
                )
                if (imageResId != 0) {
                    binding.ivOrderItemImage.setImageResource(imageResId)
                } else {
                    binding.ivOrderItemImage.setImageResource(R.drawable.ic_placeholder_default)
                }
            } else {
                binding.ivOrderItemImage.setImageResource(R.drawable.ic_placeholder_default)
            }
            android.util.Log.d("OrderAdapter", "Binding order item: ${orderItem.productName}")
        }
    }
}

class OrderItemDiffCallback : DiffUtil.ItemCallback<OrderItem>() {
    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem):
            Boolean = oldItem.productId == newItem.productId

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem):
            Boolean = oldItem == newItem
} 