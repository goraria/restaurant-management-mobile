package com.example.restaurantmanagementapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private var items: MutableList<CategoryItem>,
    private val onAddItem: (CategoryItem) -> Unit,
    private val onEditCategory: (CategoryItem) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName: TextView = view.findViewById(R.id.tv_category_name)
        val tvItemCount: TextView = view.findViewById(R.id.tv_item_count)
        val viewStatus: View = view.findViewById(R.id.view_status)
        val btnAddItem: ImageButton = view.findViewById(R.id.btn_add_item)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvCategoryName.text = item.categoryName
        holder.tvItemCount.text = item.itemCount.toString()
        
        // Đổi màu status dựa vào trạng thái
        holder.viewStatus.setBackgroundResource(
            when (item.status) {
                "Tạm ẩn" -> R.drawable.status_background_red
                else -> R.drawable.status_background_green
            }
        )

        // Xử lý thêm món vào danh mục
        holder.btnAddItem.setOnClickListener {
            onAddItem(item)
        }

        // Xử lý sửa danh mục
        holder.btnEdit.setOnClickListener {
            onEditCategory(item)
        }
    }

    override fun getItemCount() = items.size

    fun getItems() = items.toList()

    fun addItem(item: CategoryItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateItem(item: CategoryItem) {
        val position = items.indexOfFirst { it.categoryName == item.categoryName }
        if (position != -1) {
            items[position] = item
            notifyItemChanged(position)
        }
    }

    fun updateItems(newItems: List<CategoryItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
