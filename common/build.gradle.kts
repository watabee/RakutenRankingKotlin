import com.alecstrong.cocoapods.gradle.plugin.CocoapodsExtension
import com.android.build.gradle.LibraryExtension
import com.squareup.sqldelight.gradle.SqlDelightExtension
import dependencies.BuildConfig
import dependencies.Deps
import dependencies.getPropertyOrEnv
import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.alecstrong.cocoapods")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

configure<LibraryExtension> {
    compileSdkVersion(BuildConfig.compileSdk)
    defaultConfig {
        minSdkVersion(BuildConfig.minSdk)
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.lifecycleExtension)
    implementation(Deps.OkHttp.logging)
    api(Deps.timber)
}

configure<KotlinMultiplatformExtension> {
    sourceSets {
        getByName("commonMain") {
            kotlin.srcDirs("build/sqldelight")
            
            dependencies {
                implementation(Deps.Kotlin.Stdlib.common)
                implementation(Deps.Kotlin.Coroutines.common)
                implementation(Deps.Kotlin.Serialization.common)
                implementation(Deps.Ktor.Client.core)
                implementation(Deps.Ktor.Client.Json.core)
            }
        }

        create("androidMain") {
            dependencies {
                implementation(Deps.Kotlin.Stdlib.jdk)
                implementation(Deps.Kotlin.Coroutines.jdk)
                implementation(Deps.Kotlin.Coroutines.android)
                implementation(Deps.Kotlin.Serialization.jdk)
                implementation(Deps.Ktor.Client.okhttp)
                implementation(Deps.Ktor.Client.Json.jvm)
                implementation(Deps.SqlDelight.android)
            }
        }

        create("iosMain") {
            dependencies {
                implementation(Deps.Kotlin.Coroutines.native)
                implementation(Deps.Kotlin.Serialization.native)
                implementation(Deps.Ktor.Client.ios)
                implementation(Deps.Ktor.Client.Json.ios)
                implementation(Deps.SqlDelight.ios)
            }
        }
    }

    android()

    @Suppress("UNCHECKED_CAST")
    (targetForCocoapods as Closure<Unit>).call(
        listOf(presets.getByName("iosArm64"), presets.getByName("iosX64")),
        "ios",
        closureOf<KotlinNativeTarget> {
            compilations.getByName("main")
                .extraOpts("-Xuse-experimental=kotlin.Experimental", "-Xuse-experimental=kotlin.ExperimentalMultiplatform")

            compilations.forEach { 
                it.extraOpts("-linker-options", "-lsqlite3")
            }
        }
    )
}

// workaround for https://youtrack.jetbrains.com/issue/KT-27170
val compileClassPath by configurations.creating

configure<SqlDelightExtension> {
    packageName = "com.github.watabee.rakutenranking.db"
}

tasks.create("setupIos", type = Exec::class) {
    workingDir = file("${rootDir}/ios")
    setCommandLine("sh", "./scripts/setup.sh", project.getPropertyOrEnv("RAKUTEN_APP_ID"))
}

// Execute './gradlew :common:generatePodspec', then generate podspec file.
configure<CocoapodsExtension> {
    version = BuildConfig.versionName
    homepage = "https://github.com/watabee/RakutenRankingKotlin"
    deploymentTarget = "11.0"
    authors = "watabee"
    license = ""
    summary = "common"
    daemon = true
}
