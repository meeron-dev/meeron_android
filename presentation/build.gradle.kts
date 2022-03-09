plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        targetSdk = Versions.targetSdk
        minSdk = Versions.minSdk
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.Compose.version
    }

    buildFeatures {
        compose = true
    }

    viewBinding {
        isEnabled = true
    }

}

dependencies {
    implementation(project(":domain"))
    implementation(Dep.Calendar.calendar)
    implementation(Dep.Compose.ui)
    implementation(Dep.Compose.tooling)
    implementation(Dep.Compose.material)
    implementation(Dep.Compose.coil)
    implementation(Dep.Compose.constraint)

    implementation(Dep.Accompanist.pager)
    implementation(Dep.Accompanist.navigationAnimation)

    implementation(Dep.AndroidX.material)
    implementation(Dep.AndroidX.Lifecycle.composeViewModel)
    implementation(Dep.AndroidX.Lifecycle.viewModel)

    implementation(Dep.Kotlin.coroutineAndroid)
    implementation(Dep.Kotlin.coroutineRx2)
    implementation(Dep.Kotlin.reflect)
    implementation(Dep.Kotlin.serialization)

    implementation(Dep.Dagger.hiltAndroid)
    implementation(Dep.Dagger.hiltLifeCycleViewModel)
    implementation(Dep.Dagger.navigationCompose)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.Kakao.login_rx)

    implementation(Dep.Square.timber)
}