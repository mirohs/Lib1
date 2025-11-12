plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "de.luh.hci.mi.lib1"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    /*publishing {
        singleVariant("release") {
            // withSourcesJar()
            // withJavadocJar()
        }
    }*/

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.mirohs"
                artifactId = "lib1"
                version = "1.0.7"
            }
        }
    }
}

afterEvaluate {
    val isCiBuild = System.getenv("CI") == "true" || System.getenv("JITPACK") == "true"

    if (isCiBuild) {
        tasks.matching { it.name.startsWith("lintVital") }.configureEach {
            enabled = false
        }

        // Optional: skip all lint tasks (not just lintVital)
        tasks.matching { it.name.startsWith("lint") }.configureEach {
            enabled = false
        }

        println("CI build detected — lint tasks disabled for faster, error-free build.")
    }
}
