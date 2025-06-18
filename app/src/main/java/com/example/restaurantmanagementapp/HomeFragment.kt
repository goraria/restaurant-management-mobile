package com.example.restaurantmanagementapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.restaurantmanagementapp.model.RestaurantTable
import com.example.restaurantmanagementapp.repository.TableRepository
import com.example.restaurantmanagementapp.util.TableSession
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var tableButtons: Array<Button>
    private val tables = mutableListOf<RestaurantTable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var table_id = TableSession.currentTableId
        tableButtons = arrayOf(
            view.findViewById(R.id.btnTable1),
            view.findViewById(R.id.btnTable2),
            view.findViewById(R.id.btnTable3),
            view.findViewById(R.id.btnTable4),
            view.findViewById(R.id.btnTable5),
            view.findViewById(R.id.btnTable6),
            view.findViewById(R.id.btnTable7),
            view.findViewById(R.id.btnTable8),
            view.findViewById(R.id.btnTable9),
            view.findViewById(R.id.btnTable10),
            view.findViewById(R.id.btnTable11),
            view.findViewById(R.id.btnTable12)
        )

        // 2) Setup click listener cho từng nút
        setupTableButtons()

        // 3) Disable hết các nút, để tránh click khi chưa có data
        tableButtons.forEach { btn ->
            btn.isEnabled = false
            btn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.azure) // màu nhạt chờ load
        }

        // 4) Lấy data từ Repository
        lifecycleScope.launch {
            try {
                val fetched = TableRepository.getTables()
                println("Fetched tables: $fetched")
                tables.clear()
                tables.addAll(fetched)
                // Enable lại các nút khi có data
                tableButtons.forEach { it.isEnabled = true }
                updateAllButtons() // Đổi màu theo status thật khi vào lại HomeFragment
            } catch (e: Exception) {
                println("Error fetching tables: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun setupTableButtons() {
        tableButtons.forEachIndexed { i, button ->
            val tableNumber = i + 1
            button.text = tableNumber.toString()
            button.setOnClickListener {
                TableSession.currentTableId = tableNumber.toLong()
                // Lấy table theo table_id thay vì index
                val table = tables.find { it.table_id == tableNumber.toLong() }
                val frag = if (table?.status == true) {
                    CartFragment().apply { arguments = bundleOf("tableNumber" to tableNumber) }
                } else {
                    MenuFragment().apply { arguments = bundleOf("tableNumber" to tableNumber) }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun updateAllButtons() {
        tables.forEach { table ->
            val index = (table.table_id - 1).toInt()
            val button = tableButtons.getOrNull(index)
            if (button != null) {
                button.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    if (table.status) R.color.Aero else R.color.azure
                )
            }
        }
    }
}
