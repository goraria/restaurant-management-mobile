package com.example.restaurantmanagementapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantmanagementapp.MainActivity
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.databinding.ActivityLoginBinding
import com.example.restaurantmanagementapp.layout.ManagerActivity
import com.example.restaurantmanagementapp.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(binding.root)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                CoroutineScope(Dispatchers.Main).launch {
                    val success = UserRepository.loginUser(email, password)
                    if (success) {
                        Toast.makeText(this@LoginActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, ManagerActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                    }
                }
//                loginUser(email, password)
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