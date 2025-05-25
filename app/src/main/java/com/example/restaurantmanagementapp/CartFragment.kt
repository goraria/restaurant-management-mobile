package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {

    private lateinit var btnAdd: Button
    private lateinit var btnPay: Button
    private lateinit var rvCartItems: RecyclerView
    private lateinit var cartAdapter: CartItemAdapter

    // Giả lập dữ liệu món ăn trong giỏ hàng (có thể truyền từ ViewModel hoặc Bundle sau này)
    private val cartItems = mutableListOf(
        MonAn("Cánh gà", 1, "Món chiên giòn", 45000),
        MonAn("Mì xào bò", 2, "Mì xào với thịt bò", 60000),
        MonAn("Lẩu thái", 1, "Cay cay ngon ngon", 150000)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gán view
        btnPay = view.findViewById(R.id.btnPay)
        btnAdd = view.findViewById(R.id.btnAdd)
        rvCartItems = view.findViewById(R.id.rvCartItems)

        // Cấu hình RecyclerView
        cartAdapter = CartItemAdapter(cartItems) { updatedItems ->
            // Callback khi dữ liệu thay đổi => bạn có thể tính lại tổng tiền tại đây
            updateTotalCost(updatedItems)
        }
        rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        rvCartItems.adapter = cartAdapter

        // Sự kiện nút Thanh toán
        btnPay.setOnClickListener {
            Toast.makeText(requireContext(), "Đã nhấn Thanh toán", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), Bill::class.java)
            startActivity(intent)
        }

        // Sự kiện nút Thêm món
        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()
        }

        // Gọi hàm để cập nhật tổng tiền lần đầu
        updateTotalCost(cartItems)
    }

    private fun updateTotalCost(items: List<MonAn>) {
        val total = items.sumOf { it.price * it.soLuong }
        val tvCost = view?.findViewById<android.widget.TextView>(R.id.tvcost)
        tvCost?.text = "%,d đ".format(total)
    }
}
