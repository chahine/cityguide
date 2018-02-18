package com.chahinem.cityguide.api

import android.app.Application
import com.chahinem.cityguide.BuildConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module class ApiModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(app: Application): OkHttpClient {
    return OkHttpClient.Builder()
        .cache(Cache(File(app.cacheDir, OKHTTP_CACHE_DIR), DISK_CACHE_SIZE))
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
          level = when {
            BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
          }
        })
        .build()
  }

  @Provides
  @Singleton
  fun provideHttpUrl() = HttpUrl.parse("https://maps.googleapis.com/maps/api/")!!

  @Provides
  @Singleton
  fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(
      moshi: Moshi,
      baseUrl: HttpUrl,
      client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  fun provideDistanceApi(retrofit: Retrofit): DistanceMatrixApi {
    return retrofit.create(DistanceMatrixApi::class.java)
  }

  companion object {
    private const val DISK_CACHE_SIZE = 50L * 1024 * 1024 // 50MB

    /**
     * Seconds before an IO connection times out.
     */
    private const val TIMEOUT = 10L
    private const val OKHTTP_CACHE_DIR = "okHttp"
  }
}