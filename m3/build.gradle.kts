plugins {
    id(libs.plugins.kotlin.kmp.get().pluginId)
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    id(libs.plugins.compiler.compose.get().pluginId)
    id(libs.plugins.maven.publish.get().pluginId)
}

kotlin {
    androidLibrary {
        namespace = "io.github.windedge.table.m3"
        compileSdk = 35
        minSdk = 21
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutine)

                api(project(":common"))
                implementation(compose.components.resources)
                implementation(compose.material3)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}
