plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "tyko"
//include("saturn-draft")

include("drivers")
include("drivers:api")
include("drivers:chicory")
include("compiler")
include("model")
include("runtime")
include("tests")
include("tests:test-data")
include("tests:typst-shared-ffi")

include("docs-generator")

include("tests:e2e-test")
include("tests:e2e-test:chicory")
include("tests:e2e-test:common")
include("tests:human-readable")
include("tests:human-readable")