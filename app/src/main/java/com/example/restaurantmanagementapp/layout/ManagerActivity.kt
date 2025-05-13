package com.example.restaurantmanagementapp.layout

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.restaurantmanagementapp.AccountFragment
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ui.dashboard.DashboardFragment
import com.example.restaurantmanagementapp.ui.management.ManagementFragment
import com.example.restaurantmanagementapp.ui.message.MessageFragment
import com.example.restaurantmanagementapp.ui.notification.NotificationFragment

class ManagerActivity : AppCompatActivity() {
    private lateinit var btndashboard: Button
    private lateinit var btnmanagement: Button
    private lateinit var btnmessage: Button
    private lateinit var btnnotification: Button
    private lateinit var btnaccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.manager)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btndashboard = findViewById(R.id.btndashboard)
        btnmanagement = findViewById(R.id.btnmanagement)
        btnmessage = findViewById(R.id.btnmessage)
        btnnotification = findViewById(R.id.btnnotification)
        btnaccount = findViewById(R.id.btnaccount)

        loadFragment(DashboardFragment())

        btndashboard.setOnClickListener { loadFragment(DashboardFragment()) }
        btnmanagement.setOnClickListener { loadFragment(ManagementFragment()) }
        btnmessage.setOnClickListener { loadFragment(MessageFragment()) }
        btnnotification.setOnClickListener { loadFragment(NotificationFragment()) }
        btnaccount.setOnClickListener { loadFragment(AccountFragment()) }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}