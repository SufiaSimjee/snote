plugins {
    alias(libs.plugins.android.application) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.10.2"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}