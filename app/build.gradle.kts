plugins {
    id("kotlin-conventions")
}

dependencies {

    implementation(libs.aws.sdk.sts)
    implementation(libs.awsjavakit.attempt)
    testImplementation(libs.bundles.tests)
//    testImplementation(libs.awsjavakit.testingutils)
}
