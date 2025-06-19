//package com.example.restaurantmanagementapp
//
//import android.app.AlertDialog
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.restaurantmanagementapp.model.Category
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.switchmaterial.SwitchMaterial
//import com.google.android.material.textfield.TextInputEditText
//import com.google.android.material.textfield.TextInputLayout
//
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [CategoryManagementFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class CategoryManagementFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var fabAddCategory: FloatingActionButton
//    private lateinit var toolbar: Toolbar
//    private lateinit var adapter: CategoryAdapter
//    private lateinit var tvTotalCategories: TextView
//    private lateinit var tvTotalItems: TextView
//    private lateinit var searchLayout: TextInputLayout
//    private lateinit var etSearch: TextInputEditText
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_category_management, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        try {
//            initializeViews(view)
//            setupToolbar()
//            setupRecyclerView()
//            setupSearch()
//            setupFab()
//            updateOverview()
//        } catch (e: Exception) {
//            Toast.makeText(context, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun initializeViews(view: View) {
//        toolbar = view.findViewById(R.id.toolbar)
//        recyclerView = view.findViewById(R.id.recycler_categories)
//        fabAddCategory = view.findViewById(R.id.fab_add_category)
//        tvTotalCategories = view.findViewById(R.id.tv_total_categories)
//        tvTotalItems = view.findViewById(R.id.tv_total_items)
//        searchLayout = view.findViewById(R.id.search_layout)
//        etSearch = view.findViewById(R.id.et_search)
//    }
//
//    private fun setupToolbar() {
//        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
//    }
//
//    private fun setupRecyclerView() {
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        adapter = CategoryAdapter(
//            items = getSampleData().toMutableList(),
//            onAddItem = { category ->
//                try {
//                    // TODO: Xử lý thêm món vào danh mục
//                    Toast.makeText(context, "Thêm món vào ${category.categoryName}", Toast.LENGTH_SHORT).show()
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Lỗi khi thêm món: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            },
//            onEditCategory = { category ->
//                try {
//                    showEditDialog(category)
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Lỗi khi sửa danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            },
//            context = requireContext()
//        )
//        recyclerView.adapter = adapter
//    }
//
//    private fun setupSearch() {
//        etSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                try {
//                    filterItems(s.toString())
//                } catch (e: Exception) {
//                    Toast.makeText(context, "Lỗi khi tìm kiếm: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//    }
//
//    private fun setupFab() {
//        fabAddCategory.setOnClickListener {
//            try {
//                showAddEditDialog(null)
//            } catch (e: Exception) {
//                Toast.makeText(context, "Lỗi khi thêm danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun updateOverview() {
//        try {
//            val items = adapter.getItems()
//            val totalCategories = items.size
//            val totalItems = items.sumOf { it.itemCount }
//
//            tvTotalCategories.text = "Tổng số danh mục: $totalCategories"
//            tvTotalItems.text = "Tổng số món: $totalItems"
//        } catch (e: Exception) {
//            Toast.makeText(context, "Lỗi khi cập nhật thông tin: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun showAddEditDialog(category: CategoryItem?) {
//        try {
//            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_category, null)
//            val dialog = AlertDialog.Builder(requireContext())
//                .setTitle(if (category == null) "Thêm danh mục mới" else "Sửa danh mục")
//                .setView(dialogView)
//                .setPositiveButton("Lưu") { _, _ ->
//                    try {
//                        val categoryName = dialogView.findViewById<TextInputEditText>(R.id.et_category_name).text.toString()
//                        val isActive = dialogView.findViewById<SwitchMaterial>(R.id.switch_status).isChecked
//
//                        if (categoryName.isBlank()) {
//                            Toast.makeText(context, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show()
//                            return@setPositiveButton
//                        }
//
//                        if (category == null) {
//                            // Thêm mới
//                            adapter.addItem(CategoryItem(
//                                categoryName = categoryName,
//                                itemCount = 0,
//                                status = if (isActive) "Đang hoạt động" else "Tạm ẩn"
//                            ))
//                        } else {
//                            // Cập nhật
//                            adapter.updateItem(category.copy(
//                                categoryName = categoryName,
//                                status = if (isActive) "Đang hoạt động" else "Tạm ẩn"
//                            ))
//                        }
//                        updateOverview()
//                    } catch (e: Exception) {
//                        Toast.makeText(context, "Lỗi khi lưu danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                .setNegativeButton("Hủy", null)
//                .create()
//
//            // Nếu là sửa, điền thông tin cũ
//            category?.let {
//                dialogView.findViewById<TextInputEditText>(R.id.et_category_name).setText(it.categoryName)
//                dialogView.findViewById<SwitchMaterial>(R.id.switch_status).isChecked = it.status == "Đang hoạt động"
//            }
//
//            dialog.show()
//        } catch (e: Exception) {
//            Toast.makeText(context, "Lỗi khi hiển thị dialog: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun showEditDialog(category: CategoryItem) {
//        showAddEditDialog(category)
//    }
//
//    private fun filterItems(query: String) {
//        try {
//            val filteredList = if (query.isEmpty()) {
//                getSampleData()
//            } else {
//                getSampleData().filter {
//                    it.categoryName.contains(query, ignoreCase = true)
//                }
//            }
//            adapter.updateItems(filteredList)
//        } catch (e: Exception) {
//            Toast.makeText(context, "Lỗi khi lọc danh mục: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun getSampleData(): List<CategoryItem> {
//        return listOf(
//            CategoryItem("Món khai vị", 5, "Đang hoạt động"),
//            CategoryItem("Món chính", 10, "Đang hoạt động"),
//            CategoryItem("Món tráng miệng", 3, "Đang hoạt động"),
//            CategoryItem("Đồ uống", 8, "Đang hoạt động"),
//            CategoryItem("Món đặc biệt", 4, "Tạm ẩn")
//        )
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment CategoryManagementFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CategoryManagementFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//
//    import android.os.Bundle
//    import android.view.LayoutInflater
//    import android.view.View
//    import android.view.ViewGroup
//    import androidx.fragment.app.Fragment
//    import androidx.recyclerview.widget.LinearLayoutManager
//    import androidx.recyclerview.widget.RecyclerView
//    import com.example.restaurantmanagementapp.model.Category
//    import com.example.restaurantmanagementapp.R
//    import android.widget.TextView
//    import kotlinx.coroutines.CoroutineScope
//    import kotlinx.coroutines.Dispatchers
//    import kotlinx.coroutines.launch
//    import com.example.restaurantmanagementapp.config.Database
//    import io.github.jan.supabase.postgrest.postgrest
//
//    class CategoryManagementFragment : Fragment() {
//        private lateinit var recyclerView: RecyclerView
//        private lateinit var adapter: CategoryAdapter
//
//        override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View? {
//            val view = inflater.inflate(R.layout.fragment_category_management, container, false)
//            recyclerView = view.findViewById(R.id.recycler_category)
//            recyclerView.layoutManager = LinearLayoutManager(context)
//            adapter = CategoryAdapter(listOf())
//            recyclerView.adapter = adapter
//            fetchCategories()
//            return view
//        }
//
//        private fun fetchCategories() {
//            CoroutineScope(Dispatchers.Main).launch {
//                try {
//                    val result = Database.client.postgrest["categories"].select()
//                    val categories = result.decodeList<Category>()
//                    adapter.updateItems(categories)
//                } catch (e: Exception) {
//                    // handle error
//                }
//            }
//        }
//
//        class CategoryAdapter(private var items: List<Category>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
//            class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//                val tvName: TextView = view.findViewById(R.id.tv_category_name)
//            }
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
//                return ViewHolder(view)
//            }
//            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//                val item = items[position]
//                holder.tvName.text = item.category_name ?: ""
//            }
//            override fun getItemCount() = items.size
//            fun updateItems(newItems: List<Category>) {
//                items = newItems
//                notifyDataSetChanged()
//            }
//        }
//    }
