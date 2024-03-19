import Dependencies.activityCompose
import Dependencies.baristaCompose
import Dependencies.collapsableToolbarItem
import Dependencies.composeBom
import Dependencies.coreKtx
import Dependencies.coreNavigation
import Dependencies.dataStore
import Dependencies.junitExtension
import Dependencies.junit
import Dependencies.lifecycleRuntimeKtx
import Dependencies.hiltAndroid
import Dependencies.hiltCompiler
import Dependencies.hiltNavigationCompose
import Dependencies.ktorClientAndroid
import Dependencies.ktorClientContentNegotiation
import Dependencies.ktorClientCore
import Dependencies.ktorClientLogging
import Dependencies.ktorSerializationKotlinJson
import Dependencies.lifecycleRuntimeCompose
import Dependencies.metricsPerformance
import Dependencies.protobufJavaLite
import Dependencies.protobufKoltinLite
import Dependencies.timberLogging
import Dependencies.webView
import Dependencies.securityCrypto
import Dependencies.splashScreen
import Dependencies.tracingProfile
import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal val oauthKeyFile = File(rootDir, "okeys.properties")
internal val oauthKeys = Properties().apply {
    load(FileInputStream(oauthKeyFile))
}

val baseUrl: String = oauthKeys.getProperty("BASE_URL", "")
val clientId: String = oauthKeys.getProperty("CLIENT_ID", "")
var clientSecret: String = oauthKeys.getProperty("CLIENT_SECRET", "")
val grantType: String = oauthKeys.getProperty("GRANT_TYPE", "")
var scope: String = oauthKeys.getProperty("SCOPE", "")
val responseType: String = oauthKeys.getProperty("RESPONSE_TYPE", "")
val redirectUri: String = oauthKeys.getProperty("REDIRECT_URL", "")
var redirectUriWithCode: String = oauthKeys.getProperty("REDIRECT_URL_WITH_CODE", "")
val authorizeUrl: String = oauthKeys.getProperty("AUTHORIZE_URL", "")
var authorizationCodeGrantUrl: String = oauthKeys.getProperty("AUTHORIZATION_CODE_GRANT_URL", "")

val kotlinVersion = "1.9.20"
val kotlinCompilerExtVersion = "1.5.5"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.gitlab.arturbosch.detekt")
    id("com.diffplug.spotless")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf")
    id("kotlinx-serialization")
    kotlin("kapt")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.1"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

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
        getByName("debug") {
            buildConfigField("String", "BASE_URL", baseUrl)
            buildConfigField("String", "CLIENT_ID", clientId)
            buildConfigField("String", "CLIENT_SECRET", clientSecret)
            buildConfigField("String","GRANT_TYPE", grantType)
            buildConfigField("String","SCOPE", scope)
            buildConfigField("String","RESPONSE_TYPE", responseType)
            buildConfigField("String","REDIRECT_URI", redirectUri)
            buildConfigField("String","REDIRECT_URL_WITH_CODE", redirectUriWithCode)
            buildConfigField("String","AUTHORIZE_URL", authorizeUrl)
            buildConfigField("String","AUTHORIZATION_CODE_GRANT_URL", authorizationCodeGrantUrl)
        }

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

    implementation(coreNavigation)

    implementation(webView)
    implementation(coreKtx)
    implementation(lifecycleRuntimeKtx)
    implementation(lifecycleRuntimeCompose)
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

    implementation(splashScreen)

    // protobuf
    implementation(dataStore)
    implementation(protobufJavaLite)
    implementation(protobufKoltinLite)

    // compose ui
    implementation(platform(composeBom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")


    implementation(hiltNavigationCompose)

    // collapsable tool bar ui artifact
    implementation(collapsableToolbarItem)

    // profiling
    implementation(tracingProfile)
    implementation(metricsPerformance)

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

tasks.create("validate") {
    dependsOn("spotlessApply")
    doLast {
        if (clientId.isBlank() || clientSecret.isBlank()) {
            throw GradleException("Client ID and Secret must not be blank")
        }
    }
}

tasks.withType<Test> {
    // Configure your test tasks if necessary
}
