package com.example.restaurantmanagementapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Food(
    val name: String,
    val description: String,
    val price: Int,
    val imageResId: Int,

)

class FoodAdapter(private val foodList: List<Food>,
                  private val onFoodClick: (Food) -> Unit) :
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
        holder.foodName.text = food.name
        holder.foodDesc.text = food.description
        holder.foodPrice.text = "${food.price} đ"
        holder.foodImage.setImageResource(food.imageResId)

        holder.foodImage.setOnClickListener {
            onFoodClick(food)
        }
    }

    override fun getItemCount(): Int = foodList.size
}
