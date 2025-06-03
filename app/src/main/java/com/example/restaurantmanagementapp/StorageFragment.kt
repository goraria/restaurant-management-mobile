package com.example.restaurantmanagementapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.Ingredient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

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
            items = mutableListOf(),
            onInputQuantity = { item, quantity -> updateItemQuantity(item, quantity) },
            onEditItem = { item -> showEditDialog(item) },
            context = requireContext()
        )
        recyclerView.adapter = adapter

        fetchIngredientsFromSupabase()

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

    private fun fetchIngredientsFromSupabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = Database.client.postgrest["ingredients"].select()
                val ingredients = result.decodeList<Ingredient>()
                adapter.updateItems(ingredients)
                updateOverview()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi tải dữ liệu kho", Toast.LENGTH_SHORT).show()
                Log.e("StorageFragment", "fetchIngredientsFromSupabase error", e)
            }
        }
    }

    private fun updateOverview() {
        val items = adapter.getItems()
        val totalProducts = items.size
        val lowStockItems = items.count { (it.quantity ?: 0.0) <= 10 }
        tvTotalProducts.text = "Tổng số sản phẩm: $totalProducts"
        tvLowStock.text = "Sản phẩm sắp hết: $lowStockItems"
    }

    private fun showAddEditDialog(item: Ingredient?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_storage_item, null)
        val etProductName = dialogView.findViewById<TextInputEditText>(R.id.et_product_name)
        val etQuantity = dialogView.findViewById<TextInputEditText>(R.id.et_quantity)
        val etUnit = dialogView.findViewById<TextInputEditText>(R.id.et_unit)
        val etNote = dialogView.findViewById<TextInputEditText>(R.id.et_note)

        // Nếu là sửa, điền thông tin cũ
        item?.let {
            etProductName.setText(it.name)
            etQuantity.setText(it.quantity?.toInt()?.toString() ?: "")
            etUnit.setText(it.unit)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (item == null) "Thêm sản phẩm mới" else "Sửa sản phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu", null) // Để custom validate
            .setNegativeButton("Hủy", null)
            .create()

        dialog.setOnShowListener {
            val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btn.setOnClickListener {
                val productName = etProductName.text?.toString()?.trim() ?: ""
                val quantity = etQuantity.text?.toString()?.toDoubleOrNull() ?: 0.0
                val unit = etUnit.text?.toString()?.trim() ?: ""
                // val note = etNote.text?.toString() // Nếu cần dùng ghi chú

                if (productName.isBlank() || quantity <= 0 || unit.isBlank()) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin hợp lệ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (item == null) {
                    addIngredientToSupabase(productName, quantity, unit)
                } else {
                    updateIngredientToSupabase(item, productName, quantity, unit)
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun addIngredientToSupabase(name: String, quantity: Double, unit: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val newIngredient = Ingredient().apply {
                    this.name = name
                    this.quantity = quantity
                    this.unit = unit
                }
                Database.client.postgrest["ingredients"].insert(newIngredient)
                fetchIngredientsFromSupabase()
                Toast.makeText(context, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateIngredientToSupabase(item: Ingredient, name: String, quantity: Double, unit: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val updateMap = mapOf(
                    "name" to name,
                    "quantity" to quantity,
                    "unit" to unit
                )
                Database.client.postgrest["ingredients"]
                    .update(updateMap) {
                        filter { item.ingredient_id?.let { eq("ingredient_id", it) } }
                    }
                fetchIngredientsFromSupabase()
                Toast.makeText(context, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItemQuantity(item: Ingredient, additionalQuantity: Double) {
        if (additionalQuantity <= 0) {
            Toast.makeText(context, "Số lượng nhập phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }
        val newQuantity = (item.quantity ?: 0.0) + additionalQuantity
        updateIngredientToSupabase(item, item.name ?: "", newQuantity, item.unit ?: "")
    }

    private fun showEditDialog(item: Ingredient) {
        showAddEditDialog(item)
    }

    private fun filterItems(query: String) {
        val allItems = adapter.getItems()
        val filteredList = if (query.isEmpty()) {
            allItems
        } else {
            allItems.filter {
                (it.name ?: "").contains(query, ignoreCase = true) ||
                (it.unit ?: "").contains(query, ignoreCase = true)
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

    @Composable
    fun getIngredients(): List<Ingredient> {
        var ingredients by remember { mutableStateOf<List<Ingredient>>(emptyList()) }
//        return adapter.getItems()
        LaunchedEffect(Unit) {
            try {
                Log.d("IngredientList", "Starting API call...")
                val result = Database.client.postgrest["ingredients"].select()
                ingredients = result.decodeList<Ingredient>()
                Log.d("IngredientList", "Fetched ${ingredients.size}")
                ingredients.forEach { ingredient ->
                    Log.d("IngredientList", "Ingredient: ${ingredient.name}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        LazyColumn {
            items(
                items = ingredients,
                key = { ingredient -> ingredient.ingredient_id!! }
            ) { ingredient ->
                Text(
                    text = "${ingredient.name}",
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (ingredients.isEmpty()) {
                item {
                    Text("Không có nguyên liệu", modifier = Modifier.padding(8.dp))
                }
            }
        }
        return ingredients
    }
}

// Adapter cho RecyclerView
class StorageAdapter(
    private var items: MutableList<Ingredient>,
    private val onInputQuantity: (Ingredient, Double) -> Unit,
    private val onEditItem: (Ingredient) -> Unit,
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
        holder.tvProductName.text = item.name ?: ""
        holder.tvQuantity.text = item.quantity?.toInt()?.toString() ?: "0"
        holder.tvUnit.text = item.unit ?: ""

        val status = if ((item.quantity ?: 0.0) <= 10) "Sắp hết" else "Đủ"
        holder.viewStatus.setBackgroundResource(
            if (status == "Sắp hết") R.drawable.status_background_red else R.drawable.status_background_green
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

    private fun showAddStockDialog(item: Ingredient) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_add_stock, null)

        dialogView.findViewById<TextView>(R.id.tv_product_name).text =
            "Nhập thêm hàng cho: ${item.name}"

        val etAddQuantity = dialogView.findViewById<TextInputEditText>(R.id.et_add_quantity)
        etAddQuantity.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

        AlertDialog.Builder(context)
            .setTitle("Nhập thêm hàng")
            .setView(dialogView)
            .setPositiveButton("Xác nhận") { _, _ ->
                val quantity = etAddQuantity.text.toString().toDoubleOrNull() ?: 0.0
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

    fun addItem(item: Ingredient) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(item: Ingredient) {
        val position = items.indexOfFirst { it.ingredient_id == item.ingredient_id }
        if (position != -1) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun updateItems(newItems: List<Ingredient>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}