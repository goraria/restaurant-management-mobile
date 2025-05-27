package com.example.restaurantmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodBillAdapter(
    private val items: MutableList<MonAn>
) : RecyclerView.Adapter<FoodBillAdapter.MonAnViewHolder>() {

    inner class MonAnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgFood: ImageView = itemView.findViewById(R.id.imgFood)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val itemQuantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonAnViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_detail, parent, false)
        return MonAnViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonAnViewHolder, position: Int) {
        val mon = items[position]
        holder.imgFood.setImageResource(mon.imgid)
        holder.tvName.text = mon.name
        holder.itemQuantity.text = "${mon.soLuong}"
        holder.tvDescription.text = mon.desc
        holder.tvPrice.text = String.format("%,d đ", mon.price)
    }

    override fun getItemCount(): Int = items.size

    fun addMonAn(mon: MonAn) {
        items.add(mon)
        notifyItemInserted(items.size - 1)
    }
}
