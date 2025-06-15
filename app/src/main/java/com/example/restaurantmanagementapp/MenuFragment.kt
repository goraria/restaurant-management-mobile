package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.repository.FoodRepository
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuFragment : Fragment() {

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

        // Load danh sách món ăn từ Supabase và truyền vào getFoods
        loadFoodsFromSupabase { foods ->
            recyclerFood.adapter = FoodAdapter(foods) { food ->
                val intent = Intent(requireContext(), FoodDetail::class.java).apply {
                    putExtra("foodID", food.menu_id)
                    putExtra("foodName", food.name)
                    putExtra("foodDesc", food.description)
                    putExtra("foodPrice", food.price)
                    Log.d("FoodDetail", "imageUrl: ${food.image_url}")
                    putExtra("foodImage", food.image_url)
                }
                startActivity(intent)

            }
        }
    }

    private fun loadFoodsFromSupabase(onResult: (List<Food>) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                CartItemRepository.getCartItemByTableAndPaid(1,false).forEach { cartItem ->
                    Log.d("MenuFragment", "CartItem: ${cartItem.menu_id} - ${cartItem.quantity} - ${cartItem.table_id} - ${cartItem.paid}")
                }
                val result = withContext(Dispatchers.IO) {
                    Database.client.postgrest["menu"].select()
                }
                val foods = result.decodeList<Food>()
                Log.d("MenuFragment", "Foods loaded: ${foods.size}")
                foods.forEach { food ->
                    Log.d("MenuFragment", "Food: ${food.menu_id} - ${food.name} - ${food.price} - ${food.stock} - ${food.image_url} - ${food.created_at} - ${food.updated_at} - ${food.description}")
                }
                onResult(foods)
            } catch (e: Exception) {
                Log.e("MenuFragment", "Error loading foods", e)
                onResult(emptyList())
            }
        }
    }
}