// Top-level build file where you can add configuration options common to all sub-projects/modules.
// kotlin verson = 1.9.25
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.4" apply false
    id("com.diffplug.spotless") version "6.21.0" apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    id ("com.google.protobuf") version "0.9.4" apply false
    id("org.jetbrains.kotlin.plugin.serialization")  version "1.9.25" apply false
}

subprojects {
    afterEvaluate {
        project.apply("../spotless.gradle")
    }
}