//package com.example.restaurantmanagementapp.network
//
//import io.github.jan.supabase.SupabaseClient
//import io.github.jan.supabase.createSupabaseClient
//import io.github.jan.supabase.gotrue.GoTrue
//import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.storage.Storage
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logging
//
//object SupabaseClient {
//    private const val SUPABASE_URL = "YOUR_SUPABASE_URL"
//    private const val SUPABASE_KEY = "YOUR_SUPABASE_ANON_KEY"
//
//    val client: SupabaseClient = createSupabaseClient(
//        supabaseUrl = SUPABASE_URL,
//        supabaseKey = SUPABASE_KEY
//    ) {
//        install(Postgrest) {
//            // Configure Postgrest if needed
//        }
//        install(GoTrue) {
//            // Configure GoTrue if needed
//        }
//        install(Storage) {
//            // Configure Storage if needed
//        }
//
//        // Add logging for debugging
//        install(Logging) {
//            level = LogLevel.ALL
//        }
//    }
//}