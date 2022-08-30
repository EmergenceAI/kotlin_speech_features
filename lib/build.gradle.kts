plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.dokka")
}

version = "0.1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "This library provides common speech features for ASR including MFCCs and filterbank energies."
        homepage = "https://github.com/XioResearchInterGalactic/kotlin_speech_features"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "KotlinSpeechFeatures"
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
