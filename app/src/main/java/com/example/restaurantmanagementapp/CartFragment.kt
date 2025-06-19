package com.example.restaurantmanagementapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.restaurantmanagementapp.model.Bill
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.repository.BillRepository
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableNumber = com.example.restaurantmanagementapp.util.TableSession.currentTableId.toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPay = view.findViewById(R.id.btnPay)
        btnAdd = view.findViewById(R.id.btnAdd)
        rvCartItems = view.findViewById(R.id.rvCartItem)

        // Load cart items và foods từ repository
        CoroutineScope(Dispatchers.Main).launch {
            var tableId = com.example.restaurantmanagementapp.util.TableSession.currentTableId
            CartItemRepository.updateTableStatusByCart(tableId)
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
            if (cartItems.isNotEmpty() && !hasNotifiedHome) {
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

        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()
        }

        // Nút Thanh toán
        btnPay.setOnClickListener {
//            if (tableNumber == 0) {
//                Toast.makeText(requireContext(), "Không xác định được bàn!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            val intent = Intent(requireContext(), Bill::class.java)
//            intent.putExtra("tableId", tableNumber) // truyền tableNumber sang Bill với key là "tableId"
//            startActivity(intent)

            ////////////////////////////////////////////////////////////////////////////////////////////////

            CoroutineScope(Dispatchers.Main).launch {
                // Chỉ cập nhật quantity cho tất cả cart item
                var allSuccess = true
                for (i in cartItems.indices) {
                    val cartItem = cartItems[i]
                    val newQuantity = cartItem.quantity ?: 1
                    val cartItemId = cartItem.cart_item_id
                    if (cartItemId != null) {
                        val updateQuantitySuccess = CartItemRepository.updateCartItemQuantity(cartItemId, newQuantity)
                        if (!updateQuantitySuccess) {
                            allSuccess = false
                        }
                    }
                }
                if (allSuccess) {
                    Toast.makeText(requireContext(), "Cập nhật số lượng thành công!", Toast.LENGTH_SHORT).show()
                    // Thêm bill vào database
                    try {
                        val total = cartItems.zip(foods).sumOf { (cart, food) -> (cart.quantity ?: 0) * (food.price ?: 0.0) }
                        val bill = Bill(
                            bill_id = null, // Để database tự sinh ID
                            user_id = com.example.restaurantmanagementapp.util.TableSession.currentUserId,
                            table_id = com.example.restaurantmanagementapp.util.TableSession.currentTableId,
                            total_amount = total.toDouble(),
                            discount_amount = 0.0,
                            final_amount = total.toDouble(),
                            payment_method = "cash",
                            payment_status = false,
                            notes = null
                        )
                        val billResult = BillRepository.addBill(bill)
                        if (billResult) {
                            Toast.makeText(requireContext(), "Đã tạo hóa đơn!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Lỗi khi tạo hóa đơn!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Lỗi khi tạo hóa đơn: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(requireContext(), "Có lỗi khi cập nhật số lượng!", Toast.LENGTH_SHORT).show()
                }
            }
            val intent = Intent(requireContext(), BillActivity::class.java)
            startActivity(intent)
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
