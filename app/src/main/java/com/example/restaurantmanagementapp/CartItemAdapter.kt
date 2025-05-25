package com.example.restaurantmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(
    private val items: MutableList<MonAn>,
    private val onCartChanged: (List<MonAn>) -> Unit // callback truyền từ CartFragment
) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvItemName)
        private val tvQua = itemView.findViewById<TextView>(R.id.tvQua)
        private val btnAdd = itemView.findViewById<Button>(R.id.btnAdd)
        private val btnMinus = itemView.findViewById<Button>(R.id.btnMinus)
        private val btnDelete = itemView.findViewById<ImageButton>(R.id.imgDelete)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tvItemPrice) // cần thêm id này

        fun bind(monAn: MonAn) {
            tvName.text = monAn.name
            tvQua.text = monAn.soLuong.toString()
            tvPrice.text = "%,d đ".format(monAn.price)

            btnAdd.setOnClickListener {
                monAn.soLuong++
                tvQua.text = monAn.soLuong.toString()
                onCartChanged(items)
            }

            btnMinus.setOnClickListener {
                if (monAn.soLuong > 1) {
                    monAn.soLuong--
                    tvQua.text = monAn.soLuong.toString()
                    onCartChanged(items)
                }
            }

            btnDelete.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    items.removeAt(pos)
                    notifyItemRemoved(pos)
                    onCartChanged(items)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_ordered, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
