package com.example.restaurantmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.Menu
import com.example.restaurantmanagementapp.R
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.repository.MenuRepository
import io.github.jan.supabase.postgrest.postgrest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecipeManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private var menuList: List<Menu> = listOf()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private fun showAddMenuDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_menu, null)
        val editName = dialogView.findViewById<android.widget.EditText>(R.id.edit_menu_name)
        val editPrice = dialogView.findViewById<android.widget.EditText>(R.id.edit_menu_price)

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Thêm món mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val name = editName.text.toString()
                val price = editPrice.text.toString().toDoubleOrNull() ?: 0.0
                addMenuToDatabase(name, price)
            }
            .setNegativeButton("Hủy", null)
            .create()

        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_management, container, false)
        recyclerView = view.findViewById(R.id.recycler_recipe)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MenuAdapter(menuList) { menu ->
            // Khi bấm vào món, mở RecipeTableActivity và truyền menu_id, menu_name
            RecipeTableActivity.start(requireContext(), menu.menu_id ?: -1L, menu.name ?: "")
        }
        recyclerView.adapter = adapter
        fetchMenus()
        // Đổi tiêu đề sang Quản lý thực đơn
        val tvTotalMenu = view.findViewById<TextView?>(R.id.tv_total_menu)
        val tvTotalRecipe = view.findViewById<TextView?>(R.id.tv_total_recipe)
        val fabAddMenu = view.findViewById<View>(R.id.fab_add_menu)
        fabAddMenu.setOnClickListener {
            showAddMenuDialog()
        }

        tvTotalMenu?.text = "Tổng số món: ${menuList.size}"
        tvTotalRecipe?.text = "Tổng số công thức: Xem chi tiết"
        return view
    }

    private fun fetchMenus() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                menuList = MenuRepository.getMenuItems()
                adapter.updateMenus(menuList)

                // Cập nhật TextView
                view?.findViewById<TextView>(R.id.tv_total_menu)?.text = "Tổng số món: ${menuList.size}"
                view?.findViewById<TextView>(R.id.tv_total_recipe)?.text = "Tổng số công thức: Xem chi tiết"
            } catch (e: Exception) {
                e.printStackTrace()
                // Có thể hiện lỗi ra giao diện nếu muốn
            }
        }
    }

    private fun addMenuToDatabase(name: String, price: Double) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val newItem = Menu(
                    menu_id = null, // để Supabase tự sinh ID
                    name = name,
                    price = price
                )
                val success = MenuRepository.addMenuItem(newItem)
                if (success) {
                    Toast.makeText(requireContext(), "Thêm món thành công!", Toast.LENGTH_SHORT).show()
                    fetchMenus()
                } else {
                    Toast.makeText(requireContext(), "Thêm món thất bại!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Có lỗi xảy ra khi thêm món!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class MenuAdapter(
        private var menus: List<Menu>,
        private val onMenuClick: (Menu) -> Unit
    ) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
        fun updateMenus(newMenus: List<Menu>) {
            menus = newMenus
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
            return MenuViewHolder(view, onMenuClick)
        }
        override fun getItemCount() = menus.size
        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            holder.bind(menus[position])
        }
        class MenuViewHolder(view: View, val onClick: (Menu) -> Unit) : RecyclerView.ViewHolder(view) {
            private val tvName: TextView = view.findViewById(R.id.tv_menu_name)
            private val tvPrice: TextView = view.findViewById(R.id.tv_menu_price)
            fun bind(menu: Menu) {
                tvName.text = menu.name ?: ""
                tvPrice.text = "Giá: ${menu.price ?: ""}"
                itemView.setOnClickListener { onClick(menu) }
            }
        }
    }
}