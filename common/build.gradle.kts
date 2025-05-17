plugins {
    id(libs.plugins.kotlin.kmp.get().pluginId)
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    id(libs.plugins.compiler.compose.get().pluginId)
    id(libs.plugins.maven.publish.get().pluginId)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutine)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "io.github.windedge.table.res"
    publicResClass = true
}
