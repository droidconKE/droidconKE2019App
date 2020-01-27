import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("io.fabric")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.firebase-perf")
}

tasks.withType<Test> {
    testLogging {
        events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true

        debug {
            events = setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
            )
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        info.events = debug.events
        info.exceptionFormat = debug.exceptionFormat

        addTestListener(object : TestListener {
            override fun beforeTest(p0: TestDescriptor?) = Unit
            override fun beforeSuite(p0: TestDescriptor?) = Unit
            override fun afterTest(desc: TestDescriptor, result: TestResult) = Unit
            override fun afterSuite(desc: TestDescriptor, result: TestResult) {
                printResults(desc, result)
            }
        })
    }
}

fun printResults(desc: TestDescriptor, result: TestResult) {
    if (desc.parent != null) {
        val output = result.run {
            "Results: $resultType (" +
                    "$testCount tests, " +
                    "$successfulTestCount successes, " +
                    "$failedTestCount failures, " +
                    "$skippedTestCount skipped" +
                    ")"
        }
        val testResultLine = "|  $output  |"
        val repeatLength = testResultLine.length
        val seperationLine = "-".repeat(repeatLength)
        println(seperationLine)
        println(testResultLine)
        println(seperationLine)
    }
}

android {

    signingConfigs {
        create("release") {
            storeFile = file("droidconKE.jks")
            storePassword = "droidconKE"
            keyAlias = "droidconKE"
            keyPassword = "droidconKE"
        }
    }
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.android254.droidconke19"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 11
        versionName = "1.0.11"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "DroidconKe-$versionName")
        vectorDrawables.useSupportLibrary = true
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.incremental" to "true")
            }
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
        animationsDisabled = true
    }

    androidExtensions {
        isExperimental = true
    }

    (kotlinOptions as KotlinJvmOptions).apply {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.appcompat:appcompat-resources:1.1.0")
    implementation("com.google.android.material:material:1.2.0-alpha01")
    // Maintain version 2.0.0-alpha2 till issue is resolved (https://issuetracker.google.com/issues/121395935)
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha2")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("org.apache.commons:commons-lang3:3.5")
    implementation("androidx.media:media:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.browser:browser:1.0.0")
    implementation("com.google.android.gms:play-services-auth:17.0.0")
    implementation("com.github.bumptech.glide:glide:4.9.0")
    implementation("de.hdodenhof:circleimageview:3.0.1")
    implementation("com.github.thomper:sweet-alert-dialog:v1.4.0")
    implementation("androidx.test.espresso:espresso-idling-resource:3.2.0")
    implementation("androidx.emoji:emoji-appcompat:1.0.0")
    implementation("androidx.core:core-ktx:1.2.0-rc01")

    //firebase Versions
    implementation("com.google.firebase:firebase-core:17.2.1")
    implementation("com.firebaseui:firebase-ui-auth:6.0.2")
    implementation("com.google.firebase:firebase-auth:19.1.0")
    implementation("com.google.firebase:firebase-database:19.2.0")
    implementation("com.google.firebase:firebase-config:19.0.3")
    implementation("com.google.firebase:firebase-crash:16.2.1")
    implementation("com.google.firebase:firebase-messaging:20.0.1")
    implementation("com.google.firebase:firebase-firestore:21.3.0")
    implementation("com.firebaseui:firebase-ui-firestore:3.3.0")
    implementation("com.google.firebase:firebase-common-ktx:19.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.1")
    implementation("com.google.firebase:firebase-perf:19.0.2")

    //architecture components Versions
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-rc02")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc02")
    implementation("androidx.lifecycle:lifecycle-livedata:2.2.0-rc02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc02")
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0-rc02")
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    kapt("androidx.room:room-compiler:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")
    implementation("androidx.room:room-rxjava2:${Versions.roomVersion}")
    implementation("androidx.room:room-rxjava2:${Versions.roomVersion}")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.0-rc02")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.0-rc02")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.0-rc02")

    //kotlin Versions
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}")
    implementation("org.jetbrains.anko:anko-common:0.10.4")
    implementation("org.koin:koin-android:${Versions.koinVersion}")
    implementation("org.koin:koin-androidx-scope:${Versions.koinVersion}")
    implementation("org.koin:koin-androidx-viewmodel:${Versions.koinVersion}")

    testImplementation("junit:junit:4.12")
    testImplementation("org.koin:koin-test:${Versions.koinVersion}")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("androidx.test:rules:1.2.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}")
    testImplementation("androidx.room:room-testing:${Versions.roomVersion}")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    testImplementation("androidx.test.espresso:espresso-intents:3.2.0")
    testImplementation("org.robolectric:robolectric:4.3")
    testImplementation("com.agoda.kakao:kakao:2.1.0")
    implementation("androidx.fragment:fragment-testing:1.2.0-rc02") {
        exclude(mapOf("group" to "androidx.test", "module" to "core"))
    }

    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.2.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("com.bartoszlipinski:disable-animations-rule:1.0.0")
    implementation("com.crashlytics.sdk.android:crashlytics:2.9.9@aar") {
        isTransitive = true
    }
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")
    implementation("androidx.fragment:fragment:1.2.0-rc02")
    implementation("androidx.fragment:fragment-ktx:1.2.0-rc02")

}
apply(mapOf("plugin" to "com.google.gms.google-services"))
