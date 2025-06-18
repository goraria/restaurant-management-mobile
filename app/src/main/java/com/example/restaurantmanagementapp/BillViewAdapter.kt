package com.example.restaurantmanagementapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantmanagementapp.model.OrderItem

class BillViewAdapter(private val billList: List<OrderItem>) : RecyclerView.Adapter<BillViewAdapter.BillViewHolder>() {
    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.tvName)
        val txtQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val txtPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val imgFood: ImageView = itemView.findViewById(R.id.imgFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val item = billList[position]
        holder.txtName.text = item.productName
        holder.txtQuantity.text = item.quantity.toString()
        holder.txtPrice.text = "% ,.0f $".format(item.productPrice * item.quantity)

        val context = holder.imgFood.context
        val imageUrl = item.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            val resId = context.resources.getIdentifier(imageUrl, "mipmap", context.packageName)
            if (resId != 0) {
                holder.imgFood.setImageResource(resId)
            } else {
                val resIdDrawable = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
                if (resIdDrawable != 0) {
                    holder.imgFood.setImageResource(resIdDrawable)
                } else {
                    try {
                        Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(holder.imgFood)
                    } catch (e: Exception) {
                        holder.imgFood.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                }
            }
        } else {
            holder.imgFood.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount() = billList.size
}
