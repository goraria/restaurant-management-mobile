package com.example.restaurantmanagementapp

import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest["users"].select {
                    columns(Columns.raw("*"))
                }
                users = result.decodeList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
    }
}