package com.example.restaurantmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoodAdapter(private val foodList: List<com.example.restaurantmanagementapp.model.Food>,
                  private val onFoodClick: (com.example.restaurantmanagementapp.model.Food) -> Unit) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodImage: ImageButton = view.findViewById(R.id.foodImage)
        val foodName: TextView = view.findViewById(R.id.foodName)
        val foodDesc: TextView = view.findViewById(R.id.foodDesc)
        val foodPrice: TextView = view.findViewById(R.id.foodPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_food_list, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.foodName.text = food.name ?: ""
        holder.foodDesc.text = food.description ?: ""
        holder.foodPrice.text = food.price?.let { "%,.0f $".format(it) } ?: ""
        // Load ảnh từ resource nội bộ bằng tên (ưu tiên mipmap, fallback drawable)
        val context = holder.foodImage.context
        val resId = if (!food.image_url.isNullOrBlank()) {
            var id = context.resources.getIdentifier(food.image_url, "mipmap", context.packageName)
            if (id == 0) {
                id = context.resources.getIdentifier(food.image_url, "drawable", context.packageName)
            }
            id
        } else 0
        if (resId != 0) {
            holder.foodImage.setImageResource(resId)
        } else {
            holder.foodImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
        holder.foodImage.setOnClickListener {
            onFoodClick(food)
        }
    }

    override fun getItemCount(): Int = foodList.size
}
