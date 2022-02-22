plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk
    composeOptions {
        kotlinCompilerExtensionVersion = Dep.Compose.version
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Dep.Compose.ui)
    implementation(Dep.Compose.tooling)
    implementation(Dep.Compose.material)
    implementation(Dep.Compose.coil)

    implementation(Dep.Accompanist.pager)

    implementation(Dep.AndroidX.Lifecycle.composeViewModel)
    implementation(Dep.AndroidX.Lifecycle.viewModel)

    implementation(Dep.Kotlin.coroutineAndroid)
    implementation(Dep.Kotlin.coroutineRx2)

    implementation(Dep.Dagger.hiltAndroid)
    implementation(Dep.Dagger.hiltLifeCycleViewModel)
    implementation(Dep.Dagger.navigationCompose)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.Kakao.login_rx)

    implementation(Dep.Square.timber)
}