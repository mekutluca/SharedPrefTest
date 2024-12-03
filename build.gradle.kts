// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(libs.gradlePlugin)
        classpath(kotlin("gradle-plugin", version = libs.versions.kotlin.get()))
        classpath(libs.jacoco.core)
        classpath(libs.jacobo.plugin)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.jacobo) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.lint) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

// https://issuetracker.google.com/issues/195035558
subprojects {
    configurations.all {
        resolutionStrategy {
            eachDependency {
                if ("org.jacoco" == requested.group) {
                    useVersion(libs.versions.jacoco.get())
                }
            }
        }
    }
}
