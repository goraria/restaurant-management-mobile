package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerCategory = view.findViewById<RecyclerView>(R.id.recyclerCategory)
        val recyclerFood = view.findViewById<RecyclerView>(R.id.recyclerFood)

        // Gắn LayoutManager
        recyclerCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerFood.layoutManager = LinearLayoutManager(requireContext())

        // Gắn Adapter
//        recyclerCategory.adapter = CategoryAdapter(getCategories())
        recyclerFood.adapter = FoodAdapter(getFoods()) { food ->
            val intent = Intent(requireContext(), FoodDetail::class.java).apply {
                putExtra("foodName", food.name)
                putExtra("foodDesc", food.description)
                putExtra("foodPrice", food.price)
                putExtra("foodImage", food.imageResId)
            }
            startActivity(intent)
        }
    }

    private fun getFoods(): List<Food> {
        return listOf(
//            Food("Gà rán", "Đùi gà và cánh gà", 99000, R.mipmap.canhga),
        )
    }

    private fun getCategories(): List<Category> {
        return listOf(
            Category("Gà", R.mipmap.canhga),
        )
    }
}

