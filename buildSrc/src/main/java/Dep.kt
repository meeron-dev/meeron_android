object Versions {
    const val compileSdk = 31
    const val buildTools = "30.0.3"

    const val minSdk = 24
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Dep {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.0"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.7.0"
        const val startUp = "androidx.startup:startup-runtime:1.1.0"
        const val material = "com.google.android.material:material:1.6.0-alpha02"

        object Activity {
            const val activity = "androidx.activity:activity-ktx:1.4.0"
            const val compose = "androidx.activity:activity-compose:1.4.0"
        }

        object Lifecycle {
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
            const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"
        }

        object Paging {
            private const val paging_version = "3.1.0"
            const val paging = "androidx.paging:paging-runtime:$paging_version"
            const val common = "androidx.paging:paging-common-ktx:$paging_version"
            const val compose = "androidx.paging:paging-compose:1.0.0-alpha14"
        }

    }

    object Compose {
        const val version = "1.0.5"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val coil = "io.coil-kt:coil-compose:1.4.0"

    }

    object Accompanist {
        const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:0.24.1-alpha"
    }

    object Kotlin {
        private const val version = "1.5.31"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"

        private const val coroutineVersion = "1.5.2"
        const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
        const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val coroutineRx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutineVersion"

    }

    object Dagger {
        private const val daggerVersion = "2.40.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$daggerVersion"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$daggerVersion"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$daggerVersion"
        const val hiltLifeCycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-rc01"
    }

    object Square {
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val okhttp3_logging = "com.squareup.okhttp3:logging-interceptor:4.9.1"
        const val serialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val timber = "com.jakewharton.timber:timber:4.7.1"
    }

    object Room {
        const val room = "androidx.room:room-runtime:2.4.1"
        const val roomKtx = "androidx.room:room-ktx:2.4.1"
        const val roomCompiler = "androidx.room:room-compiler:2.4.1"
        const val paging = "androidx.room:room-paging:2.4.1"
    }

    object Kakao {
        const val login_rx = "com.kakao.sdk:v2-user-rx:2.8.6"
    }

    const val inject = "javax.inject:javax.inject:1"
}