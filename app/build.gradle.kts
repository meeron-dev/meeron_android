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

}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(Dep.AndroidX.startUp)

    implementation(Dep.Dagger.hiltAndroid)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.Kakao.login_rx)
    implementation(Dep.Square.timber)
}