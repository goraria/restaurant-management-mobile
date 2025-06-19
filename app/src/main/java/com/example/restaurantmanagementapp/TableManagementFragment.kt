package com.example.restaurantmanagementapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.RestaurantTable
import com.example.restaurantmanagementapp.repository.TableRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableManagementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TableAdapter
    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_management, container, false)

        recyclerView = view.findViewById(R.id.recycler_table)
        emptyTextView = view.findViewById(R.id.tv_empty_table)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = TableAdapter(listOf(), onEdit = { table ->
            showTableDialog(table)
        }, onRefresh = {
            fetchTables()
        })
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

        AlertDialog.Builder(requireContext())
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
            .show()
    }

    private fun addTable(name: String, chair: Int, status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            val success = TableRepository.addTable(
                RestaurantTable(
                    name = name,
                    chair_number = chair,
                    status = status
                )
            )
            if (success) {
                fetchTables()
                Toast.makeText(requireContext(), "Đã thêm bàn mới", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Lỗi thêm bàn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateTable(tableId: Long?, name: String, chair: Int, status: Boolean) {
        if (tableId == null) return
        CoroutineScope(Dispatchers.Main).launch {
            val success = TableRepository.updateTable(
                RestaurantTable(
                    table_id = tableId,
                    name = name,
                    chair_number = chair,
                    status = status
                )
            )
            if (success) {
                fetchTables()
                Toast.makeText(requireContext(), "Đã cập nhật bàn", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Lỗi cập nhật bàn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTables() {
        CoroutineScope(Dispatchers.Main).launch {
            val tables = TableRepository.getTables()
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
        }
    }

    class TableAdapter(
        private var items: List<RestaurantTable>,
        val onEdit: (RestaurantTable) -> Unit,
        val onRefresh: () -> Unit
    ) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tv_table_name)
            val tvChair: TextView = view.findViewById(R.id.tv_table_chair)
            val btnEdit: ImageButton = view.findViewById(R.id.btn_edit_table)
            val statusDot: View = view.findViewById(R.id.view_table_status_dot)
            val btnDelete: ImageButton = view.findViewById(R.id.btn_delete_table)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.tvName.text = item.name ?: ""
            holder.tvChair.text = "Số ghế: ${item.chair_number ?: "-"}"

            holder.statusDot.setBackgroundResource(
                if (item.status == true) R.drawable.dot_red else R.drawable.dot_green
            )

            holder.btnEdit.setOnClickListener {
                onEdit(item)
            }

            holder.btnDelete.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Xóa bàn")
                    .setMessage("Bạn có chắc chắn muốn xóa bàn '${item.name}' không?")
                    .setPositiveButton("Xóa") { _, _ ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val success = TableRepository.deleteTable(item.table_id!!)
                            if (success) {
                                Toast.makeText(holder.itemView.context, "Đã xóa bàn", Toast.LENGTH_SHORT).show()
                                onRefresh()
                            } else {
                                Toast.makeText(holder.itemView.context, "Lỗi xóa bàn", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }
        }

        override fun getItemCount() = items.size

        fun updateItems(newItems: List<RestaurantTable>) {
            items = newItems
            notifyDataSetChanged()
        }
    }
}
