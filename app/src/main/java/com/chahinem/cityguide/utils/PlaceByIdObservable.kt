package com.chahinem.cityguide.utils

import android.app.Activity
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import io.reactivex.Observable
import io.reactivex.Observer
import java.lang.Exception

class PlaceByIdObservable(activity: Activity, private val placeId: String?) : Observable<Place>() {

  private val geoDataClient = Places.getGeoDataClient(activity, null)

  override fun subscribeActual(observer: Observer<in Place>?) {
    geoDataClient.getPlaceById(placeId).addOnCompleteListener {
      if (it.isSuccessful) {
        it.result.forEach {
          observer?.onNext(it)
        }
        observer?.onComplete()
      } else {
        observer?.onError(it.exception ?: Exception("Failed retrieving place for $placeId"))
        observer?.onComplete()
      }
    }
  }
}
