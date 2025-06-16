package com.example.restaurantmanagementapp

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.restaurantmanagementapp.model.Category
import com.example.restaurantmanagementapp.model.Ingredient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.jan.supabase.postgrest.postgrest
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
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
    private var categoryList: List<Category> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        fetchCategoriesFromSupabase() // Lấy danh sách category trước

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

    private fun fetchCategoriesFromSupabase() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val result = Database.client.postgrest["categories"].select()
                categoryList = result.decodeList<Category>()
            } catch (e: Exception) {
                categoryList = emptyList()
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
        val spCategory = dialogView.findViewById<Spinner>(R.id.sp_category)
        val etPrice = dialogView.findViewById<TextInputEditText>(R.id.et_price_ingredient)

        val categoryNames = categoryList.map { it.category_name ?: "" }
        val adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapterSpinner

        // Nếu là sửa, điền thông tin cũ
        item?.let {
            etProductName.setText(it.name)
            etQuantity.setText(it.quantity?.toString() ?: "")
            etUnit.setText(it.unit)
            etPrice.setText(it.price_ingredient?.toString() ?: "")
            val selectedIndex = categoryList.indexOfFirst { cat -> cat.category_id == it.category_id }
            if (selectedIndex >= 0) spCategory.setSelection(selectedIndex)
            etNote.setText("") // Nếu DB có trường ghi chú thì lấy ra, còn không thì để trống
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (item == null) "Thêm sản phẩm mới" else "Sửa sản phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu", null)
            .setNegativeButton("Hủy", null)
            .apply {
                if (item != null) {
                    setNeutralButton("Xóa") { _, _ ->
                        showDeleteConfirmDialog(item)
                    }
                }
            }
            .create()

        dialog.setOnShowListener {
            val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btn.setOnClickListener {
                val productName = etProductName.text?.toString()?.trim() ?: ""
                val quantity = etQuantity.text?.toString()?.toDoubleOrNull() ?: 0.0
                val unit = etUnit.text?.toString()?.trim() ?: ""
                val price = etPrice.text?.toString()?.toDoubleOrNull() ?: 0.0
                val selectedCategoryIndex = spCategory.selectedItemPosition
                val categoryId = if (selectedCategoryIndex >= 0 && selectedCategoryIndex < categoryList.size) categoryList[selectedCategoryIndex].category_id else null
                // val note = etNote.text?.toString() // Nếu DB có trường ghi chú thì lấy ra

                if (productName.isBlank() || quantity <= 0 || unit.isBlank() || categoryId == null || price <= 0) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin hợp lệ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (item == null) {
                    addIngredientToSupabase(productName, quantity, unit, categoryId, price)
                } else {
                    updateIngredientToSupabase(item, productName, quantity, unit, categoryId, price)
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showDeleteConfirmDialog(item: Ingredient) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa sản phẩm")
            .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
            .setPositiveButton("Xóa") { _, _ ->
                deleteIngredientFromSupabase(item)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteIngredientFromSupabase(item: Ingredient) {
        if (item.ingredient_id == null) {
            Toast.makeText(context, "Không xác định được ID sản phẩm để xóa", Toast.LENGTH_SHORT).show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                Database.client.postgrest["ingredients"].delete {
                    filter { eq("ingredient_id", item.ingredient_id!!) }
                }
                fetchIngredientsFromSupabase()
                Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addIngredientToSupabase(name: String, quantity: Double, unit: String, categoryId: Long?, price: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val newIngredient = com.example.restaurantmanagementapp.model.Ingredient().apply {
                    this.name = name
                    this.quantity = quantity
                    this.unit = unit
                    this.category_id = categoryId
                    this.price_ingredient = price
                }
                Database.client.postgrest["ingredients"].insert(newIngredient)
                fetchIngredientsFromSupabase()
                Toast.makeText(context, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sửa hàm updateIngredientToSupabase để nhận thêm updatedAt
    private fun updateIngredientToSupabase(item: com.example.restaurantmanagementapp.model.Ingredient, name: String, quantity: Double, unit: String, categoryId: Long?, price: Double, updatedAt: String? = null) {
        if (item.ingredient_id == null) {
            Toast.makeText(context, "Không xác định được ID sản phẩm để cập nhật", Toast.LENGTH_SHORT).show()
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val updateMap = mutableMapOf<String, Any>(
                    "name" to name,
                    "quantity" to quantity,
                    "unit" to unit,
                    "category_id" to (categoryId ?: 0L),
                    "price_ingredient" to price
                )
                if (!updatedAt.isNullOrBlank()) {
                    updateMap["updated_at"] = updatedAt
                }
                Database.client.postgrest["ingredients"]
                    .update(updateMap) {
                        filter { eq("ingredient_id", item.ingredient_id!!) }
                    }
                fetchIngredientsFromSupabase()
                Toast.makeText(context, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi khi cập nhật sản phẩm: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("StorageFragment", "updateIngredientToSupabase error", e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateItemQuantity(item: Ingredient, additionalQuantity: Double) {
        if (additionalQuantity <= 0) {
            Toast.makeText(context, "Số lượng nhập phải lớn hơn 0", Toast.LENGTH_SHORT).show()
            return
        }
        val newQuantity = (item.quantity ?: 0.0) + additionalQuantity
        val now = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        updateIngredientToSupabase(
            item,
            item.name ?: "",
            newQuantity,
            item.unit ?: "",
            item.category_id,
            item.price_ingredient ?: 0.0,
            now // truyền updated_at
        )
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
            if (status == "Sắp hết") R.drawable.dot_red else R.drawable.dot_green
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
        etAddQuantity.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        val dialog = AlertDialog.Builder(context)
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
            .create()

        dialog.setOnDismissListener {
            // Ẩn bàn phím và clear focus triệt để khi đóng dialog nhập số lượng
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            val parentActivity = context as? AppCompatActivity
            val etSearch = parentActivity?.findViewById<TextInputEditText>(R.id.et_search)
            etSearch?.clearFocus()
            imm?.hideSoftInputFromWindow(etSearch?.windowToken, 0)
        }
        dialog.show()
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