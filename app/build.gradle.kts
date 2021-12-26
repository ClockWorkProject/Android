plugins {
    id("com.android.application")
    kotlin("android")
    id("com.mikepenz.aboutlibraries.plugin") version Versions.about
}

android {
    compileSdk = AppConfig.targetSdk

    defaultConfig {
        applicationId = "de.lucas.clockwork_android"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

    implementation("androidx.core:core-ktx:${Versions.core}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.google.android.material:material:${Versions.material}")

    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.navigation:navigation-compose:${Versions.composeNav}")
    implementation("androidx.compose.material:material:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Versions.compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.composeLiveData}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayout}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")

    implementation("com.chargemap.compose:numberpicker:${Versions.numberPicker}")

    implementation("com.jakewharton.timber:timber:${Versions.timber}")

    implementation("com.mikepenz:aboutlibraries-compose:${Versions.about}")

    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}")

    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.junitExt}")
}