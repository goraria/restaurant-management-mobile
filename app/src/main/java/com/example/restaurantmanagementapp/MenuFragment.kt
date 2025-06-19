package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

    private var hasNotifiedHome = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tableId = com.example.restaurantmanagementapp.util.TableSession.currentTableId
        val tableNumber = tableId.toInt()

        val recyclerCategory = view.findViewById<RecyclerView>(R.id.recyclerCategory)
        val recyclerFood = view.findViewById<RecyclerView>(R.id.recyclerFood)

        recyclerCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerFood.layoutManager = LinearLayoutManager(requireContext())

        loadFoodsFromSupabase { foods ->
            recyclerFood.adapter = FoodAdapter(foods) { food ->

                // Gửi sự kiện cho HomeFragment đổi màu bàn (nếu chưa gửi)
                if (!hasNotifiedHome) {
                    hasNotifiedHome = true
                    parentFragmentManager.setFragmentResult(
                        "tableStatusChanged",
                        bundleOf(
                            "tableNumber" to tableNumber,
                            "isOccupied" to true
                        )
                    )
                }

                // Mở màn hình FoodDetail
                val intent = Intent(requireContext(), FoodDetail::class.java).apply {
                    putExtra("foodID", food.menu_id)
                    putExtra("foodName", food.name)
                    putExtra("foodDesc", food.description)
                    putExtra("foodPrice", food.price)
                    putExtra("foodImage", food.image_url)
                    putExtra("tableID", tableId)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadFoodsFromSupabase(onResult: (List<Food>) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                CartItemRepository.getCartItemByTableAndPaid(1,false).forEach { cartItem ->
                }
                val result = withContext(Dispatchers.IO) {
                    Database.client.postgrest["menu"].select()
                }
                val foods = result.decodeList<Food>()
                onResult(foods)
            } catch (e: Exception) {
                Log.e("MenuFragment", "Error loading foods", e)
                onResult(emptyList())
            }
        }
    }
}