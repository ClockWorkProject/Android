plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("com.mikepenz.aboutlibraries.plugin") version Versions.about
    id("com.google.gms.google-services")
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
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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
    implementation("androidx.compose.runtime:runtime-livedata:${Versions.compose}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayout}")
    implementation("com.google.firebase:firebase-auth-ktx:${Versions.firebase_auth}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.compose}")

    implementation(platform("com.google.firebase:firebase-bom:${Versions.firebase_bom}"))
    implementation("com.google.firebase:firebase-database-ktx")

    implementation("com.chargemap.compose:numberpicker:${Versions.numberPicker}")

    implementation("com.jakewharton.timber:timber:${Versions.timber}")

    implementation("com.mikepenz:aboutlibraries-compose:${Versions.about}")

    implementation("com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}")
    implementation("com.google.accompanist:accompanist-pager:${Versions.accompanist}")

    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofit}")

    implementation("com.google.code.gson:gson:${Versions.gson}")

    testImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.junitExt}")
}