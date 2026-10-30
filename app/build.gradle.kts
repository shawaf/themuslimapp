plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.compose.plugin)
}

android {
    namespace = "me.shawaf.themuslimapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.shawaf.themuslimapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "3.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Google Maps key for the Qibla satellite map. Provide MAPS_API_KEY via
        // gradle properties or env; the Qibla map only renders when it is set.
        val mapsApiKey = (project.findProperty("MAPS_API_KEY") as String?)
            ?: System.getenv("MAPS_API_KEY") ?: ""
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
    }

    signingConfigs {
        // Release signing is supplied via env vars or gradle properties so no
        // secrets live in the repo. Provide RELEASE_STORE_FILE / _PASSWORD /
        // RELEASE_KEY_ALIAS / RELEASE_KEY_PASSWORD (e.g. in ~/.gradle/gradle.properties).
        create("release") {
            val storeFilePath = System.getenv("RELEASE_STORE_FILE")
                ?: (project.findProperty("RELEASE_STORE_FILE") as String?)
            if (storeFilePath != null) {
                storeFile = file(storeFilePath)
                storePassword = System.getenv("RELEASE_STORE_PASSWORD")
                    ?: (project.findProperty("RELEASE_STORE_PASSWORD") as String?)
                keyAlias = System.getenv("RELEASE_KEY_ALIAS")
                    ?: (project.findProperty("RELEASE_KEY_ALIAS") as String?)
                keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
                    ?: (project.findProperty("RELEASE_KEY_PASSWORD") as String?)
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val hasKeystore = System.getenv("RELEASE_STORE_FILE") != null ||
                project.hasProperty("RELEASE_STORE_FILE")
            if (hasKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Compose / UI
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.annotationprocessor)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    //Coroutines
    implementation(libs.android.coroutines)

    //Parser
    implementation(libs.gson)

    //Prayer times + location
    implementation(libs.adhan)
    implementation(libs.play.services.location)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //Maps (Qibla satellite view)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)

    //Home-screen widget (Jetpack Glance) + periodic refresh
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)
    implementation(libs.work.runtime.ktx)

    //Logging
    implementation(libs.timber)

    //Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}