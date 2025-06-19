package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.restaurantmanagementapp.ui.authentication.LoginActivity
import com.example.restaurantmanagementapp.util.SessionManager

class AccountFragment : Fragment() {

    private lateinit var btnedit: Button
    private lateinit var btnlogout: Button

    private lateinit var tvName: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManager = SessionManager(requireContext())
        val user = sessionManager.getUser()

        // Ánh xạ các TextView từ XML
        tvName = view.findViewById(R.id.tv_name)
        tvRole = view.findViewById(R.id.tv_role)
        tvEmail = view.findViewById(R.id.tvemail)
        tvPhone = view.findViewById(R.id.tvphone)

        // Hiển thị thông tin user
        user?.let {
            tvName.text = it.first_name
            tvRole.text = if (it.role.toInt() == 1) "Quản lý" else "Nhân viên"
            tvEmail.text = it.email
            tvPhone.text = it.phone_number
        }

        // Nút Edit
        btnedit = view.findViewById(R.id.btnedit)
        btnedit.setOnClickListener {
            val intent = Intent(requireContext(), EditAcc::class.java)
            startActivity(intent)
        }

        // Nút Logout
        btnlogout = view.findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener {
            sessionManager.clearUser()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
