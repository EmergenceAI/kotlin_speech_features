plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

val libraryVersionName = "0.1.0"
val libraryGroupName = "org.merlyn"
val libraryArtifactName = "KotlinSpeechFeatures"

group = libraryGroupName
version = libraryVersionName

kotlin {
    android {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "This library provides common speech features for ASR including MFCCs and filterbank energies."
        homepage = "https://github.com/MerlynMind/kotlin_speech_features"
        authors = "Raquib-ul Alam, Arjun Sunil, Rob Smith"
        license = "MIT License"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = libraryArtifactName
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.github.paramsen:noise:2.0.0")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }

    // Ignore duplicates
    val publicationsFromMainHost =
        listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
    publishing {
        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 27
        targetSdk = 32
    }
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            // Include Markdown docs for toplevel and packages
            includes.from("packages.md")

            // Don't publish implementation details for any source sets
            perPackageOption {
                reportUndocumented.set(false)
            }
        }
    }
    moduleName.set("Kotlin Speech Features")
    outputDirectory.set(buildDir.resolve("dokka"))
}

afterEvaluate {
    publishing {
        publications {
            withType<MavenPublication> {
                groupId = libraryGroupName
                artifactId = libraryArtifactName
                version = libraryVersionName
            }
        }
    }
}
