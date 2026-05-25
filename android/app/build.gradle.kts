import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
}

// Load signing config from environment or local.properties
val keystoreProps = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    keystoreProps.load(FileInputStream(localPropsFile))
}

android {
    namespace = "com.miabisuteri.admin"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.miabisuteri.admin"
        minSdk = 26
        targetSdk = 35
        versionCode = 4
        versionName = "1.2.3"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // GitHub repo for auto-update checks
        buildConfigField("String", "GITHUB_OWNER", "\"Dioklez\"")
        buildConfigField("String", "GITHUB_REPO", "\"Mia-Bisuteria\"")
    }

    signingConfigs {
        create("release") {
            // CI: env vars take priority; local: uses local.properties
            storeFile = (System.getenv("KEYSTORE_FILE") ?: keystoreProps.getProperty("storeFile"))
                ?.let { file(it) }
            storePassword = System.getenv("STORE_PASS") ?: keystoreProps.getProperty("storePassword")
            keyAlias = System.getenv("KEY_ALIAS") ?: keystoreProps.getProperty("keyAlias")
            keyPassword = System.getenv("KEY_PASS") ?: keystoreProps.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    debugImplementation(libs.androidx.ui.tooling)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.analytics)

    // Images
    implementation(libs.coil.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Biometric
    implementation(libs.androidx.biometric)

    // Encrypted prefs
    implementation(libs.androidx.security.crypto)

    // WorkManager (for background update checks)
    implementation(libs.androidx.work.runtime)

    // HTTP (GitHub API for update checks)
    implementation(libs.okhttp)
}
