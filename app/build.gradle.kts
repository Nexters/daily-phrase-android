import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.crashlytics)
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.silvertown.android.dailyphrase"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        applicationId = "com.silvertown.android.dailyphrase"
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            manifestPlaceholders["KAKAO_APP_KEY"] = getKey("kakao.app-key")
            buildConfigField("String", "KAKAO_APP_KEY", getKey("kakao.app-key"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.bundles.coroutines)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.timber)
    implementation(libs.hilt.android)
    implementation(libs.androidx.appcompat)
    implementation(libs.kakao)

    implementation(libs.kakao.link)

    kapt(libs.hilt.compiler)

    implementation(libs.androidx.core.splashscreen)
}

fun getKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}
