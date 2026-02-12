pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "tyko"
//include("saturn-draft")

include("drivers")
include("drivers:api")
include("drivers:chicory")
include("drivers:chicory-syntax-only")
include("drivers:chicory-formatter-only")
include("compiler")
include("model")
include("runtime")
include("tests")

include("docs-generator")

include("tests:e2e-test")
include("tests:e2e-test:chicory")
include("tests:e2e-test:common")
include("tests:human-readable")
include("tests:human-readable")

// Avoid GAV collision with :drivers:chicory
project(":tests:e2e-test:chicory").name = "chicory-e2e"
