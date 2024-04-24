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
        substitute(module("io.github.windedge.table:table")).using(project(":common"))
        substitute(module("io.github.windedge.table:table-m2")).using(project(":m2"))
        substitute(module("io.github.windedge.table:table-m3")).using(project(":m3"))
    }
}

rootProject.name = "Sample"
include(":composeApp")

