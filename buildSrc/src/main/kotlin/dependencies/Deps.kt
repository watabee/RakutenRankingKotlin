package dependencies

object Deps {
    object Kotlin {
        object Stdlib {
            val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
            val jdk = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        }

        object Coroutines {
            const val common =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${Versions.kotlinCoroutines}"
            const val jdk =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
            const val native =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${Versions.kotlinCoroutines}"
        }

        object Serialization {
            const val common =
                "org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:${Versions.kotlinSerialization}"
            const val jdk =
                "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlinSerialization}"
            const val native =
                "org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:${Versions.kotlinSerialization}"
        }
    }

    object Ktor {
        object Client {
            const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
            const val okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
            const val ios = "io.ktor:ktor-client-ios:${Versions.ktor}"

            object Json {
                const val core = "io.ktor:ktor-client-json:${Versions.ktor}"
                const val jvm = "io.ktor:ktor-client-json-jvm:${Versions.ktor}"
                const val ios = "io.ktor:ktor-client-json-native:${Versions.ktor}"
            }
        }
    }

    object SqlDelight {
        const val android = "com.squareup.sqldelight:android-driver:${Versions.sqldelight}"
        const val ios = "com.squareup.sqldelight:ios-driver:${Versions.sqldelight}"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.0.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        const val ktx = "androidx.core:core-ktx:1.0.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val lifecycleExtension =
            "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
        const val browser = "androidx.browser:browser:1.0.0"
    }

    object Koin {
        const val core = "org.koin:koin-core:${Versions.koin}"
        const val android = "org.koin:koin-android:${Versions.koin}"
        const val viewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    }

    object OkHttp {
        const val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    }

    object Groupie {
        const val core = "com.xwray:groupie:${Versions.groupie}"
        const val dataBinding = "com.xwray:groupie-databinding:${Versions.groupie}"
    }

    object Glide {
        const val core = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}