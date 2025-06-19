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
 * Use the [MenuManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter

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
        val view = inflater.inflate(R.layout.fragment_menu_management, container, false)
        recyclerView = view.findViewById(R.id.recycler_menu)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MenuAdapter(listOf())
        recyclerView.adapter = adapter
        fetchMenus()
        return view
    }

    private fun fetchMenus() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = Database.client.postgrest["menu"].select()
                val menus = result.decodeList<Menu>()
                adapter.updateItems(menus)
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    class MenuAdapter(private var items: List<Menu>) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tv_menu_name)
            val tvPrice: TextView = view.findViewById(R.id.tv_menu_price)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)

            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name ?: ""
            holder.tvPrice.text = item.price?.toString() ?: ""
        }
        override fun getItemCount() = items.size
        fun updateItems(newItems: List<Menu>) {
            items = newItems
            notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuManagementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

// File này đã được gộp vào RecipeManagementFragment.kt. Không sử dụng nữa.