package com.example.restaurantmanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.restaurantmanagementapp.model.User
import com.example.restaurantmanagementapp.repository.UserRepository
import com.example.restaurantmanagementapp.util.SessionManager
import kotlinx.coroutines.launch

class EditAcc : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var btnEdit: Button
    private lateinit var edName: EditText
    private lateinit var edEmail: EditText
    private lateinit var edPhone: EditText
    private lateinit var edOldPass: EditText
    private lateinit var edNewPass: EditText
    private lateinit var edRePass: EditText

    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_account)

        btnBack = findViewById(R.id.btnback)
        btnEdit = findViewById(R.id.btnedit)
        edName = findViewById(R.id.edname)
        edPhone = findViewById(R.id.edphone)
        edEmail = findViewById(R.id.edemail)
        edOldPass = findViewById(R.id.edOldPass)
        edNewPass = findViewById(R.id.edNewPass)
        edRePass = findViewById(R.id.edRePass)

        // 👉 Đọc user từ SessionManager
        val sessionManager = SessionManager(this)
        currentUser = sessionManager.getUser()

        if (currentUser != null) {
            showUserData(currentUser!!)
        } else {
            Toast.makeText(this, "Không có thông tin người dùng!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnEdit.setOnClickListener {
            updateAccount()
        }
    }

    private fun showUserData(user: User) {
        edName.setText("${user.first_name} ${user.last_name}")
        edEmail.setText(user.email)
        edPhone.setText(user.phone_number)
    }

    private fun updateAccount() {
        val name = edName.text.toString().trim()
        val email = edEmail.text.toString().trim()
        val phone = edPhone.text.toString().trim()
        val oldPass = edOldPass.text.toString()
        val newPass = edNewPass.text.toString()
        val rePass = edRePass.text.toString()

        if (currentUser == null) {
            Toast.makeText(this, "User không hợp lệ!", Toast.LENGTH_SHORT).show()
            return
        }

        val (firstName, lastName) = splitFullName(name)

        if (newPass.isNotEmpty() || rePass.isNotEmpty()) {
            if (oldPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu cũ!", Toast.LENGTH_SHORT).show()
                return
            }

            lifecycleScope.launch {
                val user = UserRepository.loginUser(currentUser!!.email, oldPass)
                if (user == null) {
                    Toast.makeText(this@EditAcc, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (newPass != rePass) {
                    Toast.makeText(this@EditAcc, "Nhập lại mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val updatedUser = currentUser!!.copy(
                    first_name = firstName,
                    last_name = lastName,
                    email = email,
                    phone_number = phone
                )
                val success = UserRepository.updateUser(updatedUser, newPass)
                if (success) {
                    // 👉 Lưu lại user mới sau khi update
                    SessionManager(this@EditAcc).saveUser(updatedUser)

                    Toast.makeText(this@EditAcc, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditAcc, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            lifecycleScope.launch {
                val updatedUser = currentUser!!.copy(
                    first_name = firstName,
                    last_name = lastName,
                    email = email,
                    phone_number = phone
                )
                val success = UserRepository.updateUser(updatedUser)
                if (success) {
                    // 👉 Lưu lại user mới sau khi update
                    SessionManager(this@EditAcc).saveUser(updatedUser)

                    Toast.makeText(this@EditAcc, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditAcc, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun splitFullName(fullName: String): Pair<String, String> {
        val parts = fullName.trim().split("\\s+".toRegex())
        return if (parts.size > 1) {
            val firstName = parts[0]
            val lastName = parts.subList(1, parts.size).joinToString(" ")
            Pair(firstName, lastName)
        } else {
            Pair(fullName, "")
        }
    }
}
