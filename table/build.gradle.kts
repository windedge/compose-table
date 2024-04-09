plugins {
    id(libs.plugins.kotlin.kmp.get().pluginId)
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    id(libs.plugins.maven.publish.get().pluginId)
}

kotlin {
    jvm()

    androidTarget {
        publishLibraryVariants("release")
    }

    iosArm64()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.coroutine)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.components.resources)
                implementation(compose.material)
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

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        this.sourceCompatibility = JavaVersion.VERSION_11
        this.targetCompatibility = JavaVersion.VERSION_11
    }
    namespace = "io.github.windedge.table"
}
