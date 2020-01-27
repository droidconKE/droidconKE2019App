// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("io.fabric.tools:gradle:1.31.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}")
        classpath("com.google.firebase:perf-plugin:1.3.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://maven.google.com/")
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

