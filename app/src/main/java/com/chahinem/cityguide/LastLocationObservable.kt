package com.chahinem.cityguide

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class LastLocationObservable(private val activity: Activity) : Observable<Location>() {

  private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

  override fun subscribeActual(observer: Observer<in Location>?) {
    observer?.onSubscribe(Listener(observer))
  }

  @SuppressLint("MissingPermission")
  inner class Listener(private val observer: Observer<in Location>?) : MainThreadDisposable() {
    init {
      fusedLocationClient.lastLocation
          .addOnSuccessListener(activity, { location ->
            if (location != null) {
              if (!isDisposed) {
                observer?.onNext(location)
              }
            }
          })
          .addOnCompleteListener {
            if (!isDisposed) {
              observer?.onComplete()
            }
          }
          .addOnFailureListener {
            if (!isDisposed) {
              observer?.onError(it)
            }
          }
    }

    override fun onDispose() {}
  }
}