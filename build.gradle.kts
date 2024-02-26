import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension


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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        extensions.configure<KotlinProjectExtension> {
            jvmToolchain(11)
        }

        /*
                extensions.configure<SourceSetContainer> {
                    getByName("main").java.srcDirs("src/main/kotlin/")
                    getByName("test").java.srcDirs("src/test/kotlin/")
                }
        */

        /*
                extensions.configure<MavenPublishBaseExtension> {
                    publishToMavenCentral(SonatypeHost.S01)
                }
        */

    }
}
