plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

import java.util.Properties
import java.io.FileInputStream

android {
    namespace = "bkh.apps.coupdemain"
    compileSdk = 35

    defaultConfig {
        applicationId = "bkh.apps.coupdemain"
        minSdk = 21
        targetSdk = 35
        versionCode = 2
        versionName = "0.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("app/keystore.properties")
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            } else {
                // Fallback pour contributeurs : utiliser le debug signing
                println("⚠️ keystore.properties not found - using debug signing for release")
                println("   (See app/keystore.properties.example to configure release signing)")
            }
        }
    }

    buildTypes {
        release {
            // Utiliser release signing si disponible, sinon debug signing
            val keystorePropertiesFile = rootProject.file("app/keystore.properties")
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    
    lint {
        abortOnError = false
    }
}

dependencies {
    // Core Library Desugaring pour java.time sur API 21+
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Material Design 3
    implementation("com.google.android.material:material:1.11.0")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // ViewPager2 pour onboarding
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Lifecycle (ViewModel, LiveData)
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    
    // Navigation Component
    val navVersion = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    
    // Room Database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Preferences
    implementation("androidx.preference:preference-ktx:1.2.1")
    
    // Tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
}
