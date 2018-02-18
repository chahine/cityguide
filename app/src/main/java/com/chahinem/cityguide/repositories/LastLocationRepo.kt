package com.chahinem.cityguide.repositories

import android.app.Activity
import android.location.Location
import com.chahinem.cityguide.utils.LastLocationObservable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastLocationRepo @Inject constructor(private val activity: Activity) {

  private var location: Location? = null

  fun lastLocation(): Observable<Location> {
    return if (location != null) {
      Observable.just(location)
    } else {
      LastLocationObservable(activity).flatMap { saveLocation(it) }
    }
  }

  private fun saveLocation(location: Location): Observable<Location> {
    this.location = location
    return Observable.just(this.location)
  }
}