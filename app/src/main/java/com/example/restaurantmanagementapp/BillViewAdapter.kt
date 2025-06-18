package com.example.restaurantmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.OrderItem

class BillViewAdapter(private val billList: List<OrderItem>) : RecyclerView.Adapter<BillViewAdapter.BillViewHolder>() {
    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.tvName)
        val txtQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val txtPrice: TextView = itemView.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val item = billList[position]
        holder.txtName.text = item.productName
        holder.txtQuantity.text = "x${item.quantity}"
        holder.txtPrice.text = "%,.0f đ".format(item.productPrice * item.quantity)
    }

    override fun getItemCount() = billList.size
}
