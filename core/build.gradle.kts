plugins {
    id("kotlin-conventions")
}

dependencies {
    implementation(libs.awsjavakit.misc)
    implementation(libs.tomlj)
    testImplementation(libs.bundles.tests)
    testImplementation(libs.jimfs)

}
