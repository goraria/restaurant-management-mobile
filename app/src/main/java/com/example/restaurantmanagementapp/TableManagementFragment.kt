package com.example.restaurantmanagementapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.RestaurantTable
import com.example.restaurantmanagementapp.R
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.restaurantmanagementapp.config.Database
import io.github.jan.supabase.postgrest.postgrest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import android.widget.CheckBox
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TableManagementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TableManagementFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TableAdapter
    private lateinit var emptyTextView: TextView

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
        val view = inflater.inflate(R.layout.fragment_table_management, container, false)
        recyclerView = view.findViewById(R.id.recycler_table)
        emptyTextView = view.findViewById(R.id.tv_empty_table)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = TableAdapter(listOf()) { table -> showTableDialog(table) }
        recyclerView.adapter = adapter
        val fabAddTable = view.findViewById<FloatingActionButton>(R.id.fab_add_table)
        fabAddTable.setOnClickListener {
            showTableDialog(null)
        }
        fetchTables()
        return view
    }

    private fun showTableDialog(table: RestaurantTable?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_table, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edt_table_name)
        val edtChair = dialogView.findViewById<EditText>(R.id.edt_table_chair)
        val cbStatus = dialogView.findViewById<CheckBox>(R.id.cb_table_status)
        if (table != null) {
            edtName.setText(table.name)
            edtChair.setText(table.chair_number?.toString() ?: "")
            cbStatus.isChecked = table.status == true
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (table == null) "Thêm bàn mới" else "Chỉnh sửa bàn")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val name = edtName.text.toString().trim()
                val chair = edtChair.text.toString().toIntOrNull() ?: 0
                val status = cbStatus.isChecked
                if (name.isNotEmpty() && chair > 0) {
                    if (table == null) {
                        addTable(name, chair, status)
                    } else {
                        updateTable(table.table_id, name, chair, status)
                    }
                } else {
                    Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin hợp lệ", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .create()
        dialog.show()
    }

    private fun addTable(name: String, chair: Int, status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val newTable = mapOf(
                    "name" to name,
                    "chair_number" to chair,
                    "status" to status
                )
                Database.client.postgrest["restaurant_table"].insert(newTable)
                fetchTables()
                Toast.makeText(requireContext(), "Đã thêm bàn mới", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi thêm bàn: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTable(tableId: Long?, name: String, chair: Int, status: Boolean) {
        if (tableId == null) return
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val updateData = mapOf(
                    "name" to name,
                    "chair_number" to chair,
                    "status" to status
                )
                Database.client.postgrest["restaurant_table"].update(updateData) {
                    filter { eq("table_id", tableId) }
                }
                fetchTables()
                Toast.makeText(requireContext(), "Đã cập nhật bàn", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Lỗi cập nhật bàn: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTables() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = Database.client.postgrest["restaurant_table"].select()
                val tables = result.decodeList<RestaurantTable>()
                Log.d("TableDebug", "Fetched tables: ${tables.size}")
                Toast.makeText(requireContext(), "Số bàn lấy được: ${tables.size}", Toast.LENGTH_SHORT).show()
                adapter.updateItems(tables)
                if (tables.isEmpty()) {
                    emptyTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("TableDebug", "Error: ${e.message}", e)
                emptyTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    class TableAdapter(private var items: List<RestaurantTable>, val onEdit: (RestaurantTable) -> Unit) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tv_table_name)
            val tvChair: TextView = view.findViewById(R.id.tv_table_chair)
            val btnEdit: ImageButton = view.findViewById(R.id.btn_edit_table)
            val statusDot: View = view.findViewById(R.id.view_table_status_dot)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name ?: ""
            holder.tvChair.text = "Số ghế: ${item.chair_number ?: "-"}"
            // Chấm màu: xanh nếu trống, đỏ nếu đang sử dụng
            if (item.status == true) {
                holder.statusDot.setBackgroundResource(R.drawable.dot_red)
            } else {
                holder.statusDot.setBackgroundResource(R.drawable.dot_green)
            }
            holder.btnEdit.setOnClickListener { onEdit(item) }
        }
        override fun getItemCount() = items.size
        fun updateItems(newItems: List<RestaurantTable>) {
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
         * @return A new instance of fragment TableManagementFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TableManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}