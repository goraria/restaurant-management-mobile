package com.example.restaurantmanagementapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StorageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StorageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddItem: FloatingActionButton
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: StorageAdapter
    private lateinit var tvTotalProducts: TextView
    private lateinit var tvLowStock: TextView
    private lateinit var searchLayout: TextInputLayout
    private lateinit var etSearch: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_storage, container, false)

        // Khởi tạo views
        toolbar = view.findViewById(R.id.toolbar)
        recyclerView = view.findViewById(R.id.recycler_storage)
        fabAddItem = view.findViewById(R.id.fab_add_item)
        tvTotalProducts = view.findViewById(R.id.tv_total_products)
        tvLowStock = view.findViewById(R.id.tv_low_stock)

        // Thiết lập toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        // Khởi tạo RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        // Khởi tạo adapter với dữ liệu mẫu và callbacks
        adapter = StorageAdapter(
            items = getSampleData().toMutableList(),
            onInputQuantity = { item, quantity ->
                updateItemQuantity(item, quantity)
            },
            onEditItem = { item ->
                showEditDialog(item)
            },
            context = requireContext()
        )
        recyclerView.adapter = adapter

        // Cập nhật thông tin tổng quan
        updateOverview()

        // Xử lý sự kiện click FAB
        fabAddItem.setOnClickListener {
            showAddEditDialog(null)
        }

        // Khởi tạo search
        searchLayout = view.findViewById(R.id.search_layout)
        etSearch = view.findViewById(R.id.et_search)

        // Xử lý tìm kiếm
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterItems(s.toString())
            }
        })

        return view
    }

    private fun updateOverview() {
        val items = adapter.getItems()
        val totalProducts = items.size
        val lowStockItems = items.count { it.status == "Sắp hết" }
        
        tvTotalProducts.text = "Tổng số sản phẩm: $totalProducts"
        tvLowStock.text = "Sản phẩm sắp hết: $lowStockItems"
    }

    private fun showAddEditDialog(item: StorageItem?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_storage_item, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (item == null) "Thêm sản phẩm mới" else "Sửa sản phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val productName = dialogView.findViewById<TextInputEditText>(R.id.et_product_name).text.toString()
                val quantity = dialogView.findViewById<TextInputEditText>(R.id.et_quantity).text.toString().toIntOrNull() ?: 0
                val unit = dialogView.findViewById<TextInputEditText>(R.id.et_unit).text.toString()
                val note = dialogView.findViewById<TextInputEditText>(R.id.et_note).text.toString()

                if (productName.isBlank() || quantity <= 0 || unit.isBlank()) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (item == null) {
                    // Thêm mới
                    adapter.addItem(StorageItem(productName, quantity, unit, getStatus(quantity)))
                } else {
                    // Cập nhật
                    adapter.updateItem(item.copy(
                        productName = productName,
                        quantity = quantity,
                        unit = unit,
                        status = getStatus(quantity)
                    ))
                }
                updateOverview()
            }
            .setNegativeButton("Hủy", null)
            .create()

        // Nếu là sửa, điền thông tin cũ
        item?.let {
            dialogView.findViewById<TextInputEditText>(R.id.et_product_name).setText(it.productName)
            dialogView.findViewById<TextInputEditText>(R.id.et_quantity).setText(it.quantity.toString())
            dialogView.findViewById<TextInputEditText>(R.id.et_unit).setText(it.unit)
        }

        dialog.show()
    }

    private fun updateItemQuantity(item: StorageItem, additionalQuantity: Int) {
        if (additionalQuantity <= 0) {
            Toast.makeText(context, "Số lượng nhập phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }

        val newQuantity = item.quantity + additionalQuantity
        adapter.updateItem(item.copy(
            quantity = newQuantity,
            status = getStatus(newQuantity)
        ))
        updateOverview()
        Toast.makeText(context, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show()
    }

    private fun getStatus(quantity: Int): String {
        return if (quantity <= 10) "Sắp hết" else "Đủ"
    }

    private fun getSampleData(): List<StorageItem> {
        return listOf(
            StorageItem("Gạo", 100, "kg", "Đủ"),
            StorageItem("Thịt heo", 50, "kg", "Đủ"),
            StorageItem("Rau cải", 20, "kg", "Sắp hết"),
            StorageItem("Nước mắm", 15, "chai", "Đủ"),
            StorageItem("Dầu ăn", 10, "chai", "Sắp hết"),
            StorageItem("Muối", 30, "kg", "Đủ"),
            StorageItem("Đường", 25, "kg", "Đủ"),
            StorageItem("Hành lá", 5, "kg", "Sắp hết")
        )
    }

    private fun showEditDialog(item: StorageItem) {
        try {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_storage_item, null)
            
            // Điền thông tin hiện tại vào dialog
            dialogView.findViewById<TextInputEditText>(R.id.et_product_name)?.setText(item.productName)
            dialogView.findViewById<TextInputEditText>(R.id.et_unit)?.setText(item.unit)
            
            // Ẩn ô nhập số lượng và ghi chú vì không cho phép sửa
            dialogView.findViewById<TextInputLayout>(R.id.layout_quantity)?.visibility = View.GONE
            dialogView.findViewById<TextInputLayout>(R.id.et_note)?.visibility = View.GONE

            AlertDialog.Builder(requireContext())
                .setTitle("Sửa thông tin sản phẩm")
                .setView(dialogView)
                .setPositiveButton("Lưu") { _, _ ->
                    try {
                        val productName = dialogView.findViewById<TextInputEditText>(R.id.et_product_name)
                            ?.text?.toString() ?: ""
                        val unit = dialogView.findViewById<TextInputEditText>(R.id.et_unit)
                            ?.text?.toString() ?: ""

                        if (productName.isBlank() || unit.isBlank()) {
                            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }

                        // Cập nhật thông tin sản phẩm (giữ nguyên số lượng)
                        adapter.updateItem(item.copy(
                            productName = productName,
                            unit = unit
                        ))
                        Toast.makeText(context, "Đã cập nhật thông tin sản phẩm", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Có lỗi xảy ra khi cập nhật thông tin", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Hủy", null)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, "Có lỗi xảy ra khi mở dialog sửa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterItems(query: String) {
        val filteredList = if (query.isEmpty()) {
            getSampleData()
        } else {
            getSampleData().filter {
                it.productName.contains(query, ignoreCase = true) ||
                it.unit.contains(query, ignoreCase = true)
            }
        }
        adapter.updateItems(filteredList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StorageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StorageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

// Data class cho item tồn kho
data class StorageItem(
    val productName: String,
    val quantity: Int,
    val unit: String,
    val status: String
)

// Adapter cho RecyclerView
class StorageAdapter(
    private var items: MutableList<StorageItem>,
    private val onInputQuantity: (StorageItem, Int) -> Unit,
    private val onEditItem: (StorageItem) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<StorageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tv_product_name)
        val tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
        val tvUnit: TextView = view.findViewById(R.id.tv_unit)
        val viewStatus: View = view.findViewById(R.id.view_status)
        val btnAddStock: ImageButton = view.findViewById(R.id.btn_add_stock)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_storage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvProductName.text = item.productName
        holder.tvQuantity.text = item.quantity.toString()
        holder.tvUnit.text = item.unit
        
        // Đổi màu status dựa vào trạng thái
        holder.viewStatus.setBackgroundResource(
            when (item.status) {
                "Sắp hết" -> R.drawable.status_background_red
                else -> R.drawable.status_background_green
            }
        )

        // Xử lý nhập thêm hàng
        holder.btnAddStock.setOnClickListener {
            showAddStockDialog(item)
        }

        // Xử lý sửa sản phẩm
        holder.btnEdit.setOnClickListener {
            onEditItem(item)
        }
    }

    private fun showAddStockDialog(item: StorageItem) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_add_stock, null)
        
        dialogView.findViewById<TextView>(R.id.tv_product_name).text = 
            "Nhập thêm hàng cho: ${item.productName}"

        AlertDialog.Builder(context)
            .setTitle("Nhập thêm hàng")
            .setView(dialogView)
            .setPositiveButton("Xác nhận") { _, _ ->
                val quantity = dialogView.findViewById<TextInputEditText>(R.id.et_add_quantity)
                    .text.toString().toIntOrNull() ?: 0
                if (quantity > 0) {
                    onInputQuantity(item, quantity)
                } else {
                    Toast.makeText(
                        context,
                        "Vui lòng nhập số lượng lớn hơn 0",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    override fun getItemCount() = items.size

    fun getItems() = items.toList()

    fun addItem(item: StorageItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(item: StorageItem) {
        val position = items.indexOfFirst { it.productName == item.productName }
        if (position != -1) {
            items[position] = item
            notifyItemChanged(position)
            }
    }

    fun updateItems(newItems: List<StorageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}