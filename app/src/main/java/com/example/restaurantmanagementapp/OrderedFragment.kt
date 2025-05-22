package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class OrderedFragment : Fragment() {
    private lateinit var btnMinus: Button
    private lateinit var btnAdd: Button
    private lateinit var btnDelete: ImageButton
    private lateinit var tvQua: TextView

    private var quality = 1;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ordered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAdd = view.findViewById(R.id.btnAdd)
        btnMinus = view.findViewById(R.id.btnMinus)
        btnDelete = view.findViewById(R.id.imgDelete)
        tvQua = view.findViewById(R.id.tvQua)

        tvQua.text = quality.toString()

        btnAdd.setOnClickListener {
            Log.d("OrderedFragment", "Đã nhấn nút tăng")
            quality++
            tvQua.text = quality.toString()
        }

        btnMinus.setOnClickListener {
            if (quality > 1) {
                quality--
                tvQua.text = quality.toString()
            } else {
                Toast.makeText(requireContext(), "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            quality = 1
            tvQua.text = quality.toString()
            Toast.makeText(requireContext(), "Đã xóa món khỏi đơn", Toast.LENGTH_SHORT).show()
        }
    }


}