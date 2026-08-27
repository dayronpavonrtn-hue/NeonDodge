plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
    namespace = "com.dayron.neondodge"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.dayron.neondodge"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes { release { isMinifyEnabled = false; proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") } }
}

dependencies { implementation("androidx.appcompat:appcompat:1.7.1") }
