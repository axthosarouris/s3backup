plugins {
    java
    jacoco
    pmd
    checkstyle
    id("com.github.spotbugs")
    id("com.diffplug.spotless")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// JaCoCo
jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "CLASS"
                value = "COVEREDRATIO"
                minimum = "1.0".toBigDecimal()
            }
            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "1.0".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

// Checkstyle
checkstyle {
    toolVersion = "10.21.1"
    configFile = rootProject.file("gradle-plugin-config/checkstyle.xml")
}

// PMD
pmd {
    toolVersion = "7.9.0"
    ruleSetFiles = files(rootProject.file("gradle-plugin-config/pmd-ruleset.xml"))
    ruleSets = listOf()
}

// SpotBugs
spotbugs {
    excludeFilter = rootProject.file("gradle-plugin-config/spotbugs-exclude.xml")
}

// Spotless
spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
    }
}
