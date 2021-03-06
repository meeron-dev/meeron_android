object Versions {
    const val compileSdk = 31
    const val buildTools = "30.0.3"

    const val minSdk = 23
    const val targetSdk = 32
    const val versionCode = 3
    const val versionName = "1.0.2"
}

object App {
    const val appKey = "fea2a94a3972c99a1d994e9970729ffe"
}

object Dep {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.2"

    object Google {
        const val firebase = "com.google.gms:google-services:4.3.10"
        const val firebaseBom = "com.google.firebase:firebase-bom:29.2.1"
        const val firebaseDynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
    }

    object AndroidX {
        const val startUp = "androidx.startup:startup-runtime:1.1.0"
        const val material = "com.google.android.material:material:1.6.0-alpha02"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-beta02"

        const val datastore = "androidx.datastore:datastore-preferences:1.0.0"

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
        const val version = "1.1.1"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val coil = "io.coil-kt:coil-compose:1.4.0"
        const val constraint = "androidx.constraintlayout:constraintlayout-compose:1.0.0"

    }

    object Accompanist {
        const val pager = "com.google.accompanist:accompanist-pager:0.24.4-alpha"
        const val pagerIndicator = "com.google.accompanist:accompanist-pager-indicators:0.24.4-alpha"
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:0.24.4-alpha"
        const val permission = "com.google.accompanist:accompanist-permissions:0.24.4-alpha"
    }

    object Kotlin {
        private const val version = "1.6.10"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"

        private const val coroutineVersion = "1.5.2"
        const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion"
        const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val coroutineRx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutineVersion"

        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:1.6.0"

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

    object Calendar {
        const val calendar = "com.github.prolificinteractive:material-calendarview:2.0.1"
    }

    object Kakao {
        const val login_rx = "com.kakao.sdk:v2-user-rx:2.8.6"
    }

    object AWS {
        const val mobileClient = "com.amazonaws:aws-android-sdk-mobile-client:2.13.5"
        const val cognito = "com.amazonaws:aws-android-sdk-cognito:2.13.5"
        const val s3 = "com.amazonaws:aws-android-sdk-s3:2.13.5"
    }

    const val inject = "javax.inject:javax.inject:1"
}