package com.example.restaurantmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.restaurantmanagementapp.model.Table
import com.example.restaurantmanagementapp.repository.TableRepository
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var tableButtons: Array<Button>
    private val tables = mutableListOf<Table>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                updateAllButtons() // Đổi màu theo status thật
            } catch (e: Exception) {
                println("Error fetching tables: ${e.message}")
                e.printStackTrace()
            }
        }

        // 5) Nghe cập nhật từ Menu/Cart, chỉ update đúng 1 bàn
        parentFragmentManager.setFragmentResultListener(
            "tableStatusChanged", viewLifecycleOwner
        ) { _, bundle ->
            val tableNumber = bundle.getInt("tableNumber")
            val isOccupied = bundle.getBoolean("isOccupied")
            println("Received tableStatusChanged: tableNumber=$tableNumber, isOccupied=$isOccupied")

            lifecycleScope.launch {
                try {
                    val idx = tables.indexOfFirst { it.table_id == tableNumber }
                    if (idx != -1) {
                        val updated = tables[idx].copy(status = isOccupied)
                        if (TableRepository.updateTable(updated)) {
                            // Chỉ cập nhật bàn vừa thay đổi, không reload toàn bộ
                            tables[idx] = updated
                            updateButton(tableNumber)
                        }
                    } else {
                        println("Table with table_id=$tableNumber not found")
                    }
                } catch (e: Exception) {
                    println("Error updating table $tableNumber: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupTableButtons() {
        tableButtons.forEachIndexed { i, button ->
            val tableNumber = i + 1
            button.text = tableNumber.toString()
            button.setOnClickListener {
                val table = tables.getOrNull(i)
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

    private fun updateButton(tableNumber: Int) {
        val index = tableNumber - 1
        val table = tables.getOrNull(index)
        val button = tableButtons.getOrNull(index)
        if (table != null && button != null) {
            button.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (table.status) R.color.Aero else R.color.azure
            )
        }
    }

    private fun updateAllButtons() {
        tableButtons.forEachIndexed { i, button ->
            val table = tables.getOrNull(i)
            val isOcc = table?.status ?: false
            button.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (isOcc) R.color.Aero else R.color.azure
            )
        }
    }
}
