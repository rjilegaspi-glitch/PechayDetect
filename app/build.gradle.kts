plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.jp.pechaydetect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jp.pechaydetect"
        // minSdk 24 (Android 7.0) chosen because:
        //   - TFLite 2.x requires API 21+; setting 24 gives headroom for GPU delegate and NNAPI
        //   - CameraX requires API 21+
        //   - API 24 covers ~95% of active Android devices (as of 2024)
        //   - Ensures no conflict between the TFLite model runtime and Android platform APIs
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    // Do not compress TFLite model files to allow direct memory-mapped access
    androidResources {
        noCompress += "tflite"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // Navigation Component
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Room database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // CameraX
    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // TensorFlow Lite
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.tensorflow.lite.task.vision)

    // Image loading
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // ViewPager2 (Onboarding)
    implementation(libs.viewpager2)

    // Kotlin extensions
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)

    // WorkManager (background tasks)
    implementation(libs.work.runtime.ktx)

    // EXIF metadata (image rotation fix)
    implementation(libs.exifinterface)
}
