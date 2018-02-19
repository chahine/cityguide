import Versions.archVersion
import Versions.daggerVersion
import Versions.junitVersion
import Versions.kotlinVersion
import Versions.okhttpVersion
import Versions.playVersion
import Versions.retrofitVersion
import Versions.rxandroidVersion
import Versions.rxjavaVersion
import Versions.supportLibraryVersion

@Suppress("MayBeConstant", "MemberVisibilityCanBePrivate", "unused")
object Deps {

  // Support Libraries
  val appcompatV7 = "com.android.support:appcompat-v7:$supportLibraryVersion"
  val design = "com.android.support:design:$supportLibraryVersion"
  val recyclerviewV7 = "com.android.support:recyclerview-v7:$supportLibraryVersion"
  val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.0-beta5"

  // Play Services
  val playServicesPlaces = "com.google.android.gms:play-services-places:$playVersion"
  val playServicesLocation = "com.google.android.gms:play-services-location:$playVersion"

  // Architecture Components
  val archCompiler = "android.arch.lifecycle:compiler:$archVersion"
  val archEx = "android.arch.lifecycle:extensions:$archVersion"
  val archJava = "android.arch.lifecycle:common-java8:$archVersion"

  // Api
  val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
  val moshi = "com.squareup.moshi:moshi-kotlin:1.5.0"
  val converterMoshi = "com.squareup.retrofit2:converter-moshi:2.3.0"
  val adapterRxjava = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
  val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
  val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
  val okio = "com.squareup.okio:okio:1.14.0"

  // Kotlin
  val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion"

  // Dagger
  val dagger = "com.google.dagger:dagger:$daggerVersion"
  val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

  // Libraries
  val rxjava = "io.reactivex.rxjava2:rxjava:$rxjavaVersion"
  val rxandroid = "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"
  val timber = "com.jakewharton.timber:timber:4.6.1"

  // Testing
  val jUnit = "junit:junit:$junitVersion"
  val mockito = "org.mockito:mockito-core:2.15.0"
  val hamcrest = "org.hamcrest:hamcrest-all:1.3"
  val robolectric = "org.robolectric:robolectric:3.7.1"

  // Dependency Group
  val rx = listOf(rxjava, rxandroid)

  val api = listOf(
      retrofit,
      converterMoshi,
      adapterRxjava,
      okhttp,
      loggingInterceptor,
      okio,
      moshi)

  val arch = listOf(archEx, archJava)

  val archAP = listOf(archCompiler)

  val supportLibs = listOf(
      appcompatV7,
      constraintLayout,
      design,
      recyclerviewV7
  )

  val testLibs = listOf(jUnit, mockito, hamcrest, robolectric)
}
