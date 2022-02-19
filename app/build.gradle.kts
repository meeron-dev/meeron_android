plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "fourtune.meeron"
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        minSdk = Versions.minSdk
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        debug {
            buildConfigField("String", "APP_KEY", "\"fea2a94a3972c99a1d994e9970729ffe\"")
            isDebuggable = true
        }
        release {
            buildConfigField("String", "APP_KEY", "\"fea2a94a3972c99a1d994e9970729ffe\"")
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.Compose.version
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(Dep.AndroidX.core)
    implementation(Dep.AndroidX.startUp)
    implementation(Dep.AndroidX.Activity.activity)
    implementation(Dep.AndroidX.Activity.compose)

    implementation(Dep.Compose.ui)
    implementation(Dep.Compose.tooling)
    implementation(Dep.Compose.material)
    implementation(Dep.Compose.coil)

    implementation(Dep.AndroidX.Lifecycle.composeViewModel)
    implementation(Dep.AndroidX.Lifecycle.viewModel)

    implementation(Dep.Kotlin.coroutineAndroid)
    implementation(Dep.Kotlin.coroutineRx2)

    implementation(Dep.Square.retrofit)
    implementation(Dep.Square.okhttp3_logging)

    implementation(Dep.Dagger.hiltAndroid)
    implementation(Dep.Dagger.hiltLifeCycleViewModel)
    implementation(Dep.Dagger.navigationCompose)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.Kakao.login_rx)

    implementation(Dep.Square.timber)
}