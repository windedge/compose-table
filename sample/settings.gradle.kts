pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

includeBuild("../.") {
    dependencySubstitution {
        substitute(module("io.github.windedge.table:table")).using(project(":table"))
    }
}

rootProject.name = "Sample"
include(":composeApp")

