package com.example.restaurantmanagementapp.config

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logging

class Database {
    companion object {
        val client: SupabaseClient = createSupabaseClient(
            supabaseUrl = "https://zucudrmxxvxfbgqdmjks.supabase.co/",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp1Y3Vkcm14eHZ4ZmJncWRtamtzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDUzMjY1OTMsImV4cCI6MjA2MDkwMjU5M30.683cHLaTSFWlFZbrpSRFLDW0qHA0Bnj1PqgVvxsywR0"
        ) {
            install(Postgrest) {
            }
//        install(GoTrue) {
//        }
            install(Storage) {
            }
//        install(Logging) {
//            level = LogLevel.ALL
//        }
        }
    }
}