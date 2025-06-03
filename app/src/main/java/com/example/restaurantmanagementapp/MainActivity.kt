//package com.example.restaurantmanagementapp
//
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.restaurantmanagementapp.config.Database
//import com.example.restaurantmanagementapp.model.User
//import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementTheme
//import io.github.jan.supabase.SupabaseClient
//import io.github.jan.supabase.postgrest.postgrest
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_main)
//        setContent {
//            RestaurantManagementTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    UserList()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun UserList() {
//    var users by remember { mutableStateOf<List<User>>(emptyList()) }
//
//    LaunchedEffect(Unit) {
//        try {
//            Log.d("UserList", "Starting API call...")
//            val result = Database.client.postgrest["users"].select()
//            users = result.decodeList<User>()
//            Log.d("UserList", "Fetched ${users.size} users")
//            users.forEach { user ->
//                Log.d("UserList", "User: ${user.first_name} ${user.last_name}")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    LazyColumn {
//        items(
//            items = users,
//            key = { user -> user.user_id }
//        ) { user ->
//            Text(
//                text = "${user.first_name} ${user.last_name}",
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//        if (users.isEmpty()) {
//            item {
//                Text("Không có dữ liệu người dùng", modifier = Modifier.padding(8.dp))
//            }
//        }
//        items(users, key = { it.user_id }) { user ->
//            Text("${user.first_name} ${user.last_name}", Modifier.padding(8.dp))
//        }
//    }
//}

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
