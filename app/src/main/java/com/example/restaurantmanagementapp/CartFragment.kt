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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.model.Bill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var btnAdd: Button
    private lateinit var btnPay: Button
    private lateinit var rvCartItems: RecyclerView
    private lateinit var cartAdapter: CartItemAdapter

    private var tableNumber: Int = 0
    private var hasNotifiedHome = false

    private val cartItems = mutableListOf<CartItem>()
    private val foods = mutableListOf<Food>()

    // Khởi launcher để nhận kết quả từ FoodDetail
//    private val foodDetailLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { data ->
//                val name = data.getStringExtra("name") ?: return@let
//                val desc = data.getStringExtra("desc") ?: ""
//                val price = data.getIntExtra("price", 0).toDouble()
//                val quantity = data.getIntExtra("quantity", 1)
//                val item = CartItem(
//
//                )
//                cartItems.add(item)
//                cartAdapter.notifyItemInserted(cartItems.size - 1)
//                rvCartItems.scrollToPosition(cartItems.lastIndex)
//                updateTotalCost()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableNumber = arguments?.getInt("tableNumber") ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPay = view.findViewById(R.id.btnPay)
        btnAdd = view.findViewById(R.id.btnAdd)
        rvCartItems = view.findViewById(R.id.rvCartItem)

        // Load cart items và foods từ repository
        CoroutineScope(Dispatchers.Main).launch {
            val tableId = 1L // hoặc lấy từ user/session
            val cartList = CartItemRepository.getCartItemByTableAndPaid(tableId, false)
            val foodList = CartItemRepository.getFoodsInCartByTableLoop(tableId)
            cartItems.clear()
            cartItems.addAll(cartList)
            foods.clear()
            foods.addAll(foodList)
            cartAdapter = CartItemAdapter(foods, cartItems) { updateTotalCost() }
            rvCartItems.layoutManager = LinearLayoutManager(requireContext())
            rvCartItems.adapter = cartAdapter
            updateTotalCost()
        }

        rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        rvCartItems.adapter = cartAdapter



        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()
        }

        // Nút Thanh toán
        btnPay.setOnClickListener {
            if (tableNumber == 0) {
                Toast.makeText(requireContext(), "Không xác định được bàn!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(requireContext(), Bill::class.java)
            intent.putExtra("tableId", tableNumber) // truyền tableNumber sang Bill với key là "tableId"
            startActivity(intent)        
            
            ////////////////////////////////////////////////////////////////////////////////////////////////
            
            CoroutineScope(Dispatchers.Main).launch {
                // Cập nhật quantity và paid=true cho tất cả cart item
                var allSuccess = true
                for (i in cartItems.indices) {
                    val cartItem = cartItems[i]
                    val newQuantity = cartItem.quantity ?: 1
                    val cartItemId = cartItem.cart_item_id
                    if (cartItemId != null) {
                        val updateQuantitySuccess = CartItemRepository.updateCartItemQuantity(cartItemId, newQuantity)
                        val updatePaidSuccess = CartItemRepository.updateCartItemPaid(cartItemId, true)
                        if (!updateQuantitySuccess || !updatePaidSuccess) {
                            allSuccess = false
                        }
                    }
                }
                if (allSuccess) {
                    Toast.makeText(requireContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
                    // Có thể reload lại cart hoặc chuyển sang màn hình khác
                } else {
                    Toast.makeText(requireContext(), "Có lỗi khi cập nhật giỏ hàng!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Cập nhật tổng tiền lần đầu
        updateTotalCost()
    }

    private fun addSelectedFood(food: Food) {
        // … thêm vào repository, adapter, update total …

        // Chỉ notify HomeFragment 1 lần (lần đầu)
        if (!hasNotifiedHome) {
            hasNotifiedHome = true
            parentFragmentManager.setFragmentResult(
                "tableStatusChanged",
                bundleOf(
                    "tableNumber" to tableNumber,
                    "isOccupied" to true
                )
            )
        }
    }

    // Tính và hiển thị tổng tiền
    private fun updateTotalCost() {
        val total = cartItems.zip(foods).sumOf { (cart, food) -> (cart.quantity ?: 0) * (food.price ?: 0.0) }
        view?.findViewById<android.widget.TextView>(R.id.tvcost)
            ?.text = "% ,.0f $".format(total)
    }
}

