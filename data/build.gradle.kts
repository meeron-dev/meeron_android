plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(Dep.Dagger.hiltAndroid)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.AndroidX.datastore)

    implementation(Dep.Kotlin.coroutineCore)
    implementation(Dep.Kotlin.serialization)

    implementation(Dep.Square.retrofit)
    implementation(Dep.Square.okhttp3_logging)
    implementation(Dep.Square.serialization)

    implementation(Dep.Room.room)
    implementation(Dep.Room.roomKtx)

    kapt(Dep.Room.roomCompiler)

    implementation(Dep.Square.timber)

}