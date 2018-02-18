package com.chahinem.cityguide

import android.app.Application
import timber.log.Timber

class CityGuideApp : Application() {
  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
