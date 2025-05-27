package com.example.restaurantmanagementapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {
    private lateinit var btnAdd: Button
    private lateinit var btnPay: Button
    private lateinit var rvCartItems: RecyclerView
    private lateinit var cartAdapter: CartItemAdapter

    // Dữ liệu giỏ hàng
    private val cartItems = mutableListOf<MonAn>()

    // Khởi launcher để nhận kết quả từ FoodDetail
    private val foodDetailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val name = data.getStringExtra("name") ?: return@let
                val desc = data.getStringExtra("desc") ?: ""
                val price = data.getIntExtra("price", 0)
                val quantity = data.getIntExtra("quantity", 1)

                // Debug
                Toast.makeText(requireContext(), "Nhận được: $name x$quantity", Toast.LENGTH_SHORT).show()

                // Tạo đối tượng MonAn và thêm vào danh sách
                val item = MonAn(name, quantity, desc, price)
                cartItems.add(item)
                cartAdapter.notifyItemInserted(cartItems.size - 1)
                rvCartItems.scrollToPosition(cartItems.lastIndex)
                updateTotalCost()
            }
        }
    }

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
        rvCartItems = view.findViewById(R.id.rvCartItem)

        // Cấu hình RecyclerView
        cartAdapter = CartItemAdapter(cartItems) { updatedItems ->
            updateTotalCost()
        }
        rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        rvCartItems.adapter = cartAdapter

//        btnAdd.setOnClickListener {
//            // TODO: thay bằng dữ liệu thực từ menu
//            val intent = Intent(requireContext(), MenuFragment::class.java)
//
//        }

        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()
        }

        // Nút Thanh toán
        btnPay.setOnClickListener {
            Toast.makeText(requireContext(), "Đã nhấn Thanh toán", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), Bill::class.java)
            startActivity(intent)

        }

        // Cập nhật tổng tiền lần đầu
        updateTotalCost()
    }

    // Tính và hiển thị tổng tiền
    private fun updateTotalCost() {
        val total = cartItems.sumOf { it.price * it.soLuong }
        view?.findViewById<android.widget.TextView>(R.id.tvcost)
            ?.text = "%,d đ".format(total)
    }
}

