package com.example.restaurantmanagementapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantmanagementapp.MainActivity
import com.example.restaurantmanagementapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                // TODO: Implement login logic with Supabase
                loginUser(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.tilEmail.error = "Vui lòng nhập email"
            return false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "Vui lòng nhập mật khẩu"
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        // TODO: Implement Supabase login
        // For now, just show a toast
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}