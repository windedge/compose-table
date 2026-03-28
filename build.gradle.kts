import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin


plugins {
    alias(libs.plugins.kotlin.kmp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.compiler.compose) apply false
    alias(libs.plugins.buildconfig) apply false
}

val groupId = properties["GROUP"].toString()
val currentVersion = properties["VERSION_NAME"].toString()

repositories {
    mavenCentral()
}

subprojects {

    repositories {
        mavenCentral()
        google()
    }

    plugins.withType<KotlinBasePlugin> {
        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(17)

            jvm()

            iosArm64()
            iosX64()
            iosSimulatorArm64()

            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                browser()
            }
            js {
                browser {
                    commonWebpackConfig {
                        showProgress = true
                    }
                }
                binaries.library()
                generateTypeScriptDefinitions()
            }
        }
    }

    afterEvaluate {

        extensions.findByType<BuildConfigExtension>()?.apply {
            val groupId = properties["GROUP"].toString()
            val currentVersion = properties["VERSION_NAME"].toString()

            packageName(groupId)
            className("Artifacts")

            buildConfigField("String", "groupId", "\"$groupId\"")
            buildConfigField("String", "currentVersion", "\"$currentVersion\"")
        }
    }
}
