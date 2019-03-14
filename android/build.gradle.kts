import com.android.build.gradle.AppExtension
import dependencies.Deps
import dependencies.BuildConfig
import dependencies.getPropertyOrEnv

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlinx-serialization")
    id("kotlin-kapt")
}

configure<AppExtension> {
    compileSdkVersion(BuildConfig.compileSdk)

    defaultConfig {
        applicationId = "com.github.watabee.rakutenranking"
        minSdkVersion(BuildConfig.minSdk)
        targetSdkVersion(BuildConfig.targetSdk)
        versionCode = BuildConfig.versionCode
        versionName  = BuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dataBinding {
        isEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true

            val files = project.file("proguards")
                .listFiles()
                .filter { it.name.startsWith("proguard") }
                .plus(getDefaultProguardFile("proguard-android-optimize.txt"))
                .toTypedArray()

            proguardFiles(*files)
        }
    }

    packagingOptions {
        // https://ktor.io/quickstart/migration/1.1.1.html
        // Known issues: There are duplicate META-INF files in common and jvm modules.
        exclude("META-INF/ktor-http.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/ktor-utils.kotlin_module")
        exclude("META-INF/kotlinx-coroutines-io.kotlin_module")
        exclude("META-INF/ktor-client-json.kotlin_module")
        exclude("META-INF/ktor-client-core.kotlin_module")
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common"))

    implementation(Deps.Kotlin.Stdlib.jdk)
    implementation(Deps.Kotlin.Coroutines.jdk)
    implementation(Deps.Kotlin.Coroutines.android)

    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.ktx)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.browser)

    implementation(Deps.SqlDelight.android)

    implementation(Deps.Groupie.core)
    implementation(Deps.Groupie.dataBinding)

    implementation(Deps.Glide.core)
    kapt(Deps.Glide.compiler)

    implementation(Deps.Koin.core)
    implementation(Deps.Koin.android)
    implementation(Deps.Koin.viewModel) {
        exclude(group = "androidx.lifecycle")
    }

    implementation(Deps.Ktor.Client.okhttp)
}
