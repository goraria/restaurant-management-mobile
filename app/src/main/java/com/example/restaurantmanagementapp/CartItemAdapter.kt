package com.example.restaurantmanagementapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.model.CartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartItemAdapter(
    private val foods: MutableList<Food>,
    private val cartItems: MutableList<CartItem>,
    private val onCartChanged: (List<CartItem>) -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvItemName)
        private val tvQua = itemView.findViewById<TextView>(R.id.tvQua)
        private val btnAdd = itemView.findViewById<Button>(R.id.btnAdd)
        private val btnMinus = itemView.findViewById<Button>(R.id.btnMinus)
        private val btnDelete = itemView.findViewById<ImageButton>(R.id.imgDelete)
        private val tvPrice = itemView.findViewById<TextView>(R.id.tvItemPrice)
        private val imgItem = itemView.findViewById<ImageView>(R.id.imgItem)

        fun bind(food: Food, cartItem: CartItem) {
            tvName.text = food.name
            tvQua.text = cartItem.quantity?.toString() ?: "0"
            tvPrice.text = "%,.0f đ".format(food.price)

            // Hiển thị ảnh theo food.image_url
            val context = itemView.context
            val imageUrl = food.image_url
            if (!imageUrl.isNullOrBlank()) {
                val resId = context.resources.getIdentifier(imageUrl, "mipmap", context.packageName)
                if (resId != 0) {
                    imgItem.setImageResource(resId)
                } else {
                    val resIdDrawable = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
                    if (resIdDrawable != 0) {
                        imgItem.setImageResource(resIdDrawable)
                    } else {
                        // Nếu là URL hoặc không tìm thấy resource nội bộ, dùng Glide
                        try {
                            com.bumptech.glide.Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(imgItem)
                        } catch (e: Exception) {
                            imgItem.setImageResource(R.drawable.ic_launcher_foreground)
                        }
                    }
                }
            } else {
                imgItem.setImageResource(R.drawable.ic_launcher_foreground)
            }

            btnAdd.setOnClickListener {
                cartItem.quantity = (cartItem.quantity ?: 0) + 1
                tvQua.text = cartItem.quantity?.toInt()?.toString() ?: "0"
                onCartChanged(cartItems)
            }

            btnMinus.setOnClickListener {
                if ((cartItem.quantity ?: 1) > 1) {
                    cartItem.quantity = (cartItem.quantity ?: 1) - 1
                    tvQua.text = cartItem.quantity?.toInt()?.toString() ?: "0"
                    onCartChanged(cartItems)
                }
            }

            btnDelete.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val cartItemId = cartItems[pos].cart_item_id
                    if (cartItemId != null) {
                        // Xóa trên server trước, sau đó xóa local nếu thành công
                        CoroutineScope(Dispatchers.Main).launch {
                            val success = CartItemRepository.deleteCartItem(cartItemId)
                            if (success) {
                                foods.removeAt(pos)
                                cartItems.removeAt(pos)
                                notifyItemRemoved(pos)
                                onCartChanged(cartItems)
                            } else {
                                Toast.makeText(itemView.context, "Xóa thất bại!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Nếu cart_item_id null, chỉ xóa local
                        foods.removeAt(pos)
                        cartItems.removeAt(pos)
                        notifyItemRemoved(pos)
                        onCartChanged(cartItems)
                    }
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
        holder.bind(foods[position], cartItems[position])
    }

    override fun getItemCount(): Int = foods.size

    fun getCartItems(): List<CartItem> = cartItems.toList()
    fun getFoods(): List<Food> = foods.toList()
}
