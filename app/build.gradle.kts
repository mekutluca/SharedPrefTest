import com.kageiit.jacobo.JacoboTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    kotlin("android")
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.jacobo)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    jacoco
}


jacoco {
    toolVersion = libs.versions.jacoco.get()
}

android {
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.sharedpreftest"
        minSdk = 26
        targetSdk = 35
        versionCode = System.getenv("BITRISE_BUILD_NUMBER")?.toInt() ?: 99999
        versionName = "1.0.$versionCode"
        // Don't attempt to crunch PNGs since there should only be vector assets of webp images
        aaptOptions.cruncherEnabled = false
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }
    buildTypes {
        getByName("release") {
            // Uncomment the below 2 lines if you want to run this build against the debug signing key
            // signingConfig = signingConfigs.getByName("debug")
            // isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "SharedPref")
            manifestPlaceholders["networkSecurityConfig"] = "network_security_config_pinned"
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            withGroovyBuilder {
                "FirebasePerformance" {
                    invokeMethod("setInstrumentationEnabled", false)
                }
            }
            resValue("string", "app_name", "SharedPref.debug")
            manifestPlaceholders["networkSecurityConfig"] = "network_security_config_unsecure"
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packagingOptions.resources {
        excludes += "META-INF/**"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs =
            listOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlinx.coroutines.DelicateCoroutinesApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                "-opt-in=androidx.compose.ui.text.ExperimentalTextApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
            )
        allWarningsAsErrors = true
    }


    lint {
        warningsAsErrors = true
        checkTestSources = true
        // We suppress lint Gradle warnings with the following code below.
        disable += "GradleDependency"
        disable += "ComposeModifierComposed"
        disable += "ComposeCompositionLocalUsage"
        disable += "AndroidGradlePluginVersion"
        disable += "OldTargetApi"
        // https://issuetracker.google.com/issues/333755527
        // https://issuetracker.google.com/issues/265962219
        disable += "EnsureInitializerMetadata"
    }
    namespace = "com.example.sharedpreftest"
}

val debugAndAlphaImplementation: Configuration by configurations.creating

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.collections.immutable)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.core)
    implementation(libs.androidx.exif)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.paging)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    debugImplementation(libs.androidx.compose.test.manifest)
    debugImplementation(libs.androidx.compose.tooling)
    implementation(libs.androidx.compose.tooling.preview)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.startup.runtime)

    ksp(libs.androidx.room.compiler)

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.sqlcipher)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.inappmessaging)
    implementation(libs.google.firebase.messaging)
    implementation(libs.google.firebase.perf)
    implementation(libs.google.firebase.remoteconfig)
    implementation(libs.google.inappreview)
    implementation(libs.google.appupdate)
    implementation(libs.google.codescanner)
    implementation(libs.google.ads)

    implementation(libs.branchsdk)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.koin.navigation.extension)

    implementation(libs.jakewharton.timber)

    implementation(libs.vital.vitalClient)
    implementation(libs.vital.vitalHealthConnect)

    implementation(libs.image.cropper)

    implementation(libs.squareup.moshi.core)
    implementation(libs.squareup.moshi.adapters)
    ksp(libs.squareup.moshi.codegen)
    implementation(libs.squareup.retrofit.core)
    implementation(libs.squareup.retrofit.moshi)
    implementation(libs.squareup.okhttp.core)
    implementation(libs.squareup.okhttp.interceptor)

    implementation(libs.svg)
    implementation(libs.scarlet.core)
    implementation(libs.scarlet.lifecycle)
    implementation(libs.scarlet.websocket)
    implementation(libs.scarlet.moshi)
    implementation(libs.konfetti)
    implementation(libs.konfetti.compose)
    implementation(libs.lottie)

    // Test
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.truth)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.architecture)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(libs.androidx.navigation.test)
    testImplementation(libs.androidx.compose.ui.test.junit)
    testImplementation(libs.androidx.test.espresso)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestUtil(libs.androidx.test.orchestrator)
    androidTestImplementation(libs.androidx.test.runner)

    debugAndAlphaImplementation(libs.facebook.soloader)
    debugAndAlphaImplementation(libs.facebook.flipper)
    debugAndAlphaImplementation(libs.facebook.flipper.network.plugin)
    debugAndAlphaImplementation(libs.facebook.flipper.leakcanary)
    debugAndAlphaImplementation(libs.leakcanary)

    lintChecks(libs.lint.compose)
    lintChecks(project(":lint"))
}

ktlint {
    version.set(libs.versions.ktlintVersion.get())
}

tasks.register<JacoboTask>("jacobo") {
    dependsOn("jacocoTestReport")
    group = "reporting"
    jacocoReport =
        File(
            "${project.projectDir}/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
    coberturaReport = File("${project.projectDir}/build/cobertura.xml")
    srcDirs[0] = "${project.projectDir}/src/main/java"
}
