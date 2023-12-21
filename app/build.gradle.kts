import Dependencies.activityCompose
import Dependencies.baristaCompose
import Dependencies.composeBom
import Dependencies.coreKtx
import Dependencies.junitExtension
import Dependencies.junit
import Dependencies.lifecycleRuntimeKtx
import Dependencies.hiltAndroid
import Dependencies.hiltCompiler
import Dependencies.timberLogging
val kotlinVersion = "1.9.20"
val kotlinCompilerExtVersion = "1.5.5"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.gitlab.arturbosch.detekt")
    id("com.diffplug.spotless")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
}

internal val oauthKeyFile = File(rootDir, "okeys.properties")
internal val oauthKeys = Properties().apply {
    load(FileInputStream(oauthKeyFile))
}

val clientId = oauthKeys.getProperty("CLIENT_ID", "")
val clientSecret = oauthKeys.getProperty("CLIENT_SECRET", "")


android {
    namespace = QioConfig.appId
    compileSdk = QioConfig.compileSdkVersion

    defaultConfig {
        applicationId = QioConfig.appId
        minSdk = QioConfig.minSdkVersion
        targetSdk = QioConfig.targetSdkVersion
        versionCode = QioConfig.versionCode
        versionName = QioConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = kotlinCompilerExtVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(webView)
    implementation(coreKtx)
    implementation(lifecycleRuntimeKtx)
    implementation(activityCompose)

    implementation(timberLogging)

    implementation(securityCrypto)

    implementation(hiltAndroid)
    kapt(hiltCompiler)

    // ktor
    implementation(ktorClientCore)
    implementation(ktorClientContentNegotiation)
    implementation(ktorSerializationKotlinJson)
    implementation(ktorClientAndroid)
    implementation(ktorClientLogging)


    // compose ui
    implementation(platform(composeBom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // AndroidTest libs
    androidTestImplementation(junitExtension)
    androidTestImplementation(baristaCompose)
    androidTestImplementation(platform(composeBom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug libs
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    testImplementation(junit)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}