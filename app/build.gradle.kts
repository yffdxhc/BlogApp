plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.nuist.blogapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.nuist.blogapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /*viewPager+tab的依赖*/
    implementation ("com.google.android.material:material:1.9.0")//提供了 TabLayout 等组件，可以与 ViewPager2 配合使用
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    /*Glide的依赖*/
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
}