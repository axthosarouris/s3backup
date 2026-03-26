plugins {
    `kotlin-dsl`
}

repositories{
    gradlePluginPortal()
    mavenCentral()
}
dependencies {

    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.4.0")
    implementation("io.kotest:io.kotest.gradle.plugin:6.1.9")
}
