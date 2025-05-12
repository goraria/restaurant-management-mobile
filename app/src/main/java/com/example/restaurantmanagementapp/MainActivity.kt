package com.example.restaurantmanagementapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.restaurantmanagementapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var btnhome: Button
    private lateinit var btnmenu: Button
    private lateinit var btncart: Button
    private lateinit var btnaccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnhome = findViewById(R.id.btnhome)
        btnmenu = findViewById(R.id.btnmenu)
        btncart = findViewById(R.id.btncart)
        btnaccount = findViewById(R.id.btnaccount)

        loadFragment(HomeFragment())

        btnhome.setOnClickListener { loadFragment(HomeFragment()) }
        btnmenu.setOnClickListener { loadFragment(MenuFragment()) }
        btncart.setOnClickListener { loadFragment(CartFragment()) }
        btnaccount.setOnClickListener { loadFragment(AccountFragment()) }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
