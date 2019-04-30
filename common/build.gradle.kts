import com.android.build.gradle.LibraryExtension
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.squareup.sqldelight.gradle.SqlDelightDatabase
import dependencies.BuildConfig
import dependencies.Deps
import dependencies.getPropertyOrEnv
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("com.codingfeline.buildkonfig")
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

version = BuildConfig.versionName

dependencies {
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.lifecycleExtension)
    implementation(Deps.OkHttp.logging)
    api(Deps.timber)
}

configure<KotlinMultiplatformExtension> {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs("build/sqldelight")
            
            dependencies {
                implementation(Deps.Kotlin.Stdlib.common)
                implementation(Deps.Kotlin.Coroutines.common)
                implementation(Deps.Kotlin.Serialization.common)
                implementation(Deps.Ktor.Client.core)
                implementation(Deps.Ktor.Client.Json.core)
            }
        }

        val androidMain by creating {
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

        val iosMain by creating {
            dependencies {
                implementation(Deps.Kotlin.Coroutines.native)
                implementation(Deps.Kotlin.Serialization.native)
                implementation(Deps.Ktor.Client.ios)
                implementation(Deps.Ktor.Client.Json.ios)
                implementation(Deps.SqlDelight.ios)
            }
        }

        val iosX64Main by creating {
            dependencies {
                dependsOn(iosMain)
            }
        }
    }

    android()

    iosArm64("ios")
    iosX64("iosX64")

    // ./gradlew podspec
    cocoapods {
        authors = "watabee"
        homepage = "https://github.com/watabee/RakutenRankingKotlin"
        summary = "common"
        license = ""
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xuse-experimental=kotlin.Experimental",
        "-Xuse-experimental=kotlin.ExperimentalMultiplatform"
    )
}

// workaround for https://youtrack.jetbrains.com/issue/KT-27170
val compileClassPath by configurations.creating

// FIXME: https://github.com/square/sqldelight/issues/1274
sqldelight {
    methodMissing("Database", arrayOf(closureOf<SqlDelightDatabase> {
        packageName = "com.github.watabee.rakutenranking.db"
        sourceFolders = listOf("sqldelight")
        schemaOutputDirectory = file("build/sqldelight")
    }))
}

tasks.create("setupIos", type = Exec::class) {
    workingDir = file("${rootDir}/ios")
    setCommandLine("sh", "./scripts/setup.sh")
}

configure<BuildKonfigExtension> {
    packageName = "com.github.watabee.rakutenranking"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "RAKUTEN_APP_ID",
            "${project.getPropertyOrEnv("RAKUTEN_APP_ID")}"
        )
    }
}

// WORKAROUND --------
val deleteTask = tasks.create("deleteBuildKonfigDir", Delete::class) {
    delete(listOf("buildkonfig", "iosX64Main").fold(project.buildDir, ::File))
}

project.tasks.whenTaskAdded {
    if (name == "generateBuildKonfig") {
        finalizedBy(deleteTask)
    }
}

project.afterEvaluate {
    val mppExtension = extensions.getByType(KotlinMultiplatformExtension::class.java)
    val targets = mppExtension.targets
    val sourceSets = mppExtension.sourceSets

    targets.filterIsInstance<KotlinNativeTarget>()
        .filter { target -> target.name != "ios" }
        .map { target -> sourceSets.getByName("${target.name}Main") }
        .forEach { sourceSetMain ->

            sourceSetMain.kotlin.setSrcDirs(
                sourceSetMain.kotlin
                    .srcDirs
                    .filter { !it.absolutePath.contains("buildkonfig") }
            )
        }
}
// ----------------------