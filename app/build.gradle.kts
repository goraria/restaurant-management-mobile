plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.restaurantmanagementapp"
    compileSdk = 35

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
        }
    }


    defaultConfig {
        applicationId = "com.example.restaurantmanagementapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }

}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Supabase
    implementation(platform(libs.bom))
    implementation(libs.supabase.postgrest.kt)
    // implementation(libs.realtime.kt)
    implementation(libs.supabase.realtime.kt)
    implementation(libs.supabase.storage.kt)
    // implementation(libs.storage.kt)
    // implementation(libs.gotrue.kt)

    // implementation(platform("io.github.jan-tennert.supabase:bom:$supabase_version"))
    // implementation("io.ktor:ktor-client-android:$ktor_version")

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)
    
    // Ktor
    implementation(libs.ktor.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback.android)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.gridlayout)

    // Testing

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Supabase Kotlin client
    // implementation("io.github.jan-tennert.supabase:postgrest-kt:1.0.0")        // Cập nhật lên phiên bản mới nhất :contentReference[oaicite:5]{index=5}
    // implementation("io.github.jan-tennert.supabase:realtime-kt:1.0.0")        // Nếu cần realtime
    // implementation("io.ktor:ktor-client-android:2.3.0")                     // Ktor Android engine :contentReference[oaicite:6]{index=6}

    // Kotlinx Serialization
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // Kotlinx DateTime
    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("org.mindrot:jbcrypt:0.4")

    // **BẮT BUỘC** phải có để dùng androidx.gridlayout.widget.GridLayout
    implementation("androidx.gridlayout:gridlayout:1.0.0")

}

