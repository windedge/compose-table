enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

include(
    ":common",
    ":m2",
    ":m3",
)

rootProject.name = "compose-table"
