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
include("drivers:chicory-based")
include("compiler")
include("model")
include("runtime")
include("tests")

include("docs-generator")

