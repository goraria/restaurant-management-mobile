package com.example.restaurantmanagementapp.ui.authentication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantmanagementapp.databinding.ActivityRegisterBinding
import com.example.restaurantmanagementapp.model.User
import com.example.restaurantmanagementapp.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInput(fullName, email, phone, password, confirmPassword)) {
                // TODO: Implement registration logic with Supabase
                registerUser(fullName, email, phone, password)
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Vui lòng nhập họ tên"
            return false
        }
        if (email.isEmpty()) {
            binding.tilEmail.error = "Vui lòng nhập email"
            return false
        }
        if (phone.isEmpty()) {
            binding.tilPhone.error = "Vui lòng nhập số điện thoại"
            return false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "Vui lòng nhập mật khẩu"
            return false
        }
        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
            return false
        }
        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Mật khẩu không khớp"
            return false
        }
        return true
    }

    private fun registerUser(firstName: String, email: String, phone: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val isAvailable = UserRepository.isUsernameAvailable(email)
            if (!isAvailable) {
                Toast.makeText(this@RegisterActivity, "Email đã tồn tại", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Tạo đối tượng User để đăng ký
            val user = User(
                user_id = 0L,
                username = email,
                email = email,
                phone_number = phone,
                password = password,
                first_name = firstName,
                last_name = "",
                profile_picture_url = null,
                address = null,
                is_active = true,
                is_verified = false,
                last_login_at = null,
                created_at = kotlinx.datetime.Clock.System.now(),
                role = 0L
            )

            val success = UserRepository.addUser(user)
            if (success) {
                Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}