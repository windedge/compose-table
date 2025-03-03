import com.android.build.api.dsl.LibraryExtension
import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin


plugins {
    alias(libs.plugins.kotlin.kmp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.compose) apply false
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

            plugins.withId("com.android.library") {
                androidTarget {
                    publishLibraryVariants("release")
                }
            }

            iosArm64()
            iosX64()
            iosSimulatorArm64()

            js {
                browser()
                binaries.executable()
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


        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        extensions.configure<LibraryExtension> {
            compileSdk = 33
            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            defaultConfig {
                minSdk = 21
            }
            compileOptions {
                this.sourceCompatibility = JavaVersion.VERSION_17
                this.targetCompatibility = JavaVersion.VERSION_17
            }
            namespace = "io.github.windedge.table"
        }


    }
}
