plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "fourtune.meeron"
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        minSdk = Versions.minSdk
        manifestPlaceholders["APP_KEY"] = App.appKey
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        debug {
            buildConfigField("String", "APP_KEY", "\"${App.appKey}\"")
            isDebuggable = true
        }
        release {
            buildConfigField("String", "APP_KEY", "\"${App.appKey}\"")
            isDebuggable = false
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

    implementation(Dep.Kotlin.coroutineCore)
    implementation(Dep.Kakao.login_rx)
    implementation(Dep.Square.timber)
}