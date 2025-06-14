package com.example.restaurantmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.User
import com.example.restaurantmanagementapp.R
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.restaurantmanagementapp.config.Database
import io.github.jan.supabase.postgrest.postgrest

/**
 * A simple [Fragment] subclass for employee management.
 * Use the [EmployeeManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmployeeManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_employee_management, container, false)
        recyclerView = view.findViewById(R.id.recycler_user)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserAdapter(listOf())
        recyclerView.adapter = adapter
        fetchUsers()
        return view
    }

    private fun fetchUsers() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = Database.client.postgrest["users"].select()
                val users = result.decodeList<User>()
                adapter.updateItems(users)
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    class UserAdapter(private var items: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tv_user_name)
            val tvFullName: TextView = view.findViewById(R.id.tv_user_fullname)
            val tvEmail: TextView = view.findViewById(R.id.tv_user_email)
            val tvPhone: TextView = view.findViewById(R.id.tv_user_phone)
            val tvRole: TextView = view.findViewById(R.id.tv_user_role)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.username
            holder.tvFullName.text = "Họ tên: ${item.first_name ?: ""} ${item.last_name ?: ""}".trim()
            holder.tvEmail.text = "Email: ${item.email}"
            holder.tvPhone.text = "SĐT: ${item.phone_number ?: ""}"
            holder.tvRole.text = "Vai trò: ${if (item.role == 0L) "Nhân viên" else "Quản lý"}"
        }
        override fun getItemCount() = items.size
        fun updateItems(newItems: List<User>) {
            items = newItems
            notifyDataSetChanged()
        }
    }
}
