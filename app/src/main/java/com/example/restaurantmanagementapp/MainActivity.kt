package com.example.restaurantmanagementapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.User
import com.example.restaurantmanagementapp.ui.theme.RestaurantManagementTheme
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        setContent {
            RestaurantManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserList()
                }
            }
        }
    }
}

@Composable
fun UserList() {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        try {
            Log.d("UserList", "Starting API call...")
            val result = Database.client.postgrest["users"].select()
            users = result.decodeList<User>()
            Log.d("UserList", "Fetched ${users.size} users")
            users.forEach { user ->
                Log.d("UserList", "User: ${user.first_name} ${user.last_name}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LazyColumn {
        items(
            items = users,
            key = { user -> user.user_id }
        ) { user ->
            Text(
                text = "${user.first_name} ${user.last_name}",
                modifier = Modifier.padding(8.dp)
            )
        }
        if (users.isEmpty()) {
            item {
                Text("Không có dữ liệu người dùng", modifier = Modifier.padding(8.dp))
            }
        }
        items(users, key = { it.user_id }) { user ->
            Text("${user.first_name} ${user.last_name}", Modifier.padding(8.dp))
        }
    }
}
