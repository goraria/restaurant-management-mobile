package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.restaurantmanagementapp.Bill


class CartFragment : Fragment() {
    private lateinit var btnAdd: Button
    private lateinit var btnPay: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnPay = view.findViewById(R.id.btnPay)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnPay.setOnClickListener {
            Toast.makeText(requireContext(), "Đã nhấn Thanh toán", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), Bill::class.java)
            startActivity(intent)

        }

        btnAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}