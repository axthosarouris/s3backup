plugins {
    id("kotlin-conventions")
}

dependencies {
    implementation(project (":local-file-listing"))
    implementation(libs.awsjavakit.misc)
    implementation(libs.tomlj)
    implementation(libs.bundles.logging)
    testImplementation(libs.bundles.tests)

}
