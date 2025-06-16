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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.restaurantmanagementapp.config.Database
import io.github.jan.supabase.postgrest.postgrest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        tvTotalMenu?.text = "Tổng số món: ${menuList.size}"
        tvTotalRecipe?.text = "Tổng số công thức: Xem chi tiết"
        return view
    }

    private fun fetchMenus() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val menuResult = Database.client.postgrest["menu"].select()
                val menus = menuResult.decodeList<Menu>()
                menuList = menus
                adapter.updateMenus(menuList)
            } catch (e: Exception) {
                // handle error
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipeManagementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecipeManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}