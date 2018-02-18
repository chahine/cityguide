package com.chahinem.cityguide.utils

import android.annotation.SuppressLint
import android.app.Activity
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import java.lang.Exception

class PlaceByIdObservable(activity: Activity, private val placeId: String?) : Observable<Place>() {

  private val geoDataClient = Places.getGeoDataClient(activity, null)

  override fun subscribeActual(observer: Observer<in Place>?) {
    observer?.onSubscribe(Listener(observer))
  }

  @SuppressLint("MissingPermission")
  inner class Listener(private val observer: Observer<in Place>?) : MainThreadDisposable() {
    init {
      geoDataClient.getPlaceById(placeId).addOnCompleteListener {
        if (it.isSuccessful) {
          it.result.forEach {
            if (!isDisposed) {
              observer?.onNext(it)
            }
          }
          if (!isDisposed) {
            observer?.onComplete()
          }
        } else {
          if (!isDisposed) {
            observer?.onError(it.exception ?: Exception("Failed retrieving place for $placeId"))
            observer?.onComplete()
          }
        }
      }
    }

    override fun onDispose() {}
  }
}