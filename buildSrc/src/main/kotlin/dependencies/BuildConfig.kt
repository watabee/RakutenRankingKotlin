package dependencies

object BuildConfig {
    const val compileSdk = 28
    const val minSdk = 21
    const val targetSdk = 28

    private const val major = 1
    private const val minor = 0
    private const val patch = 0
    private const val build = 0

    const val versionName = "$major.$minor.$patch"
    const val versionFullName = "$versionName.$build"
    const val versionCode = major * 1_000_000 + minor * 10_000 + patch * 100 + build
}
