package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditAcc : AppCompatActivity() {

    private lateinit var btnback: Button
    private lateinit var btnedit: Button
    private lateinit var edname: EditText
    private lateinit var edemail: EditText
    private lateinit var edphone: EditText
    private lateinit var edOldPass: EditText
    private lateinit var edNewPass: EditText
    private lateinit var edRePass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_account)

        btnback = findViewById(R.id.btnback)
        btnedit = findViewById(R.id.btnedit)
        edname = findViewById(R.id.edname)
        edphone = findViewById(R.id.edphone)
        edemail = findViewById(R.id.edemail)
        edOldPass = findViewById(R.id.edOldPass)
        edNewPass = findViewById(R.id.edNewPass)
        edRePass = findViewById(R.id.edRePass)



    }



}