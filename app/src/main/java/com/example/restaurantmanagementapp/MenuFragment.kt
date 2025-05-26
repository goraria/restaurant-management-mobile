package com.example.restaurantmanagementapp

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

        setupCategoryAdapter(recyclerCategory)
        recyclerFood.adapter = FoodAdapter(getFoods())
    }

    private fun setupCategoryAdapter(recyclerView: RecyclerView) {
        try {
            // Chuyển đổi List<Category> thành MutableList<CategoryItem>
            val categoryItems = getCategories().map { cat ->
                CategoryItem(
                    categoryName = cat.name,
                    itemCount = getFoods().count { it.category == cat.name },
                    status = "Đang hoạt động" // Mặc định là đang hoạt động cho menu
                )
            }.toMutableList()

            categoryAdapter = CategoryAdapter(
                items = categoryItems,
                onAddItem = { categoryItem ->
                    try {
                        // TODO: Xử lý thêm món vào danh mục
                        Toast.makeText(context, "Thêm món vào ${categoryItem.categoryName}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Lỗi khi thêm món: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                onEditCategory = { categoryItem ->
                    try {
                        // TODO: Xử lý sửa danh mục
                        Toast.makeText(context, "Sửa danh mục ${categoryItem.categoryName}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Lỗi khi sửa danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                context = requireContext()
            )
            recyclerView.adapter = categoryAdapter
        } catch (e: Exception) {
            Toast.makeText(context, "Lỗi khi thiết lập danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Giả lập dữ liệu món ăn
    private fun getFoods(): List<Food> {
        return listOf(
            Food(
                name = "Gà rán",
                description = "Đùi gà và cánh gà",
                price = 99000,
                imageResId = R.drawable.ic_launcher_background,
                category = "Gà"
            ),
            Food(
                name = "Gà nướng",
                description = "Gà nướng nguyên con",
                price = 199000,
                imageResId = R.drawable.ic_launcher_background,
                category = "Gà"
            )
        )
    }

    private fun getCategories(): List<Category> {
        return listOf(
            Category("Gà", R.drawable.ic_launcher_foreground),
            Category("Món khai vị", R.drawable.ic_launcher_foreground),
            Category("Món chính", R.drawable.ic_launcher_foreground),
            Category("Món tráng miệng", R.drawable.ic_launcher_foreground),
            Category("Đồ uống", R.drawable.ic_launcher_foreground)
        )
    }
}

