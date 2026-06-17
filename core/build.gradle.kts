plugins {
    id("kotlin-conventions")
}

dependencies {
    implementation(libs.awsjavakit.misc)
    implementation(libs.tomlj)
    implementation(libs.bundles.logging)
    testImplementation(libs.bundles.tests)

}
