package com.chahinem.cityguide.utils

import android.app.Activity
import android.location.Location
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class AutocompletePredictionsObservable(
    activity: Activity,
    private val query: String,
    private val location: Location,
    private val range: Double = .01) : Observable<AutocompletePrediction>() {

  private val geoDataClient = Places.getGeoDataClient(activity, null)

  override fun subscribeActual(observer: Observer<in AutocompletePrediction>?) {
    observer?.onSubscribe(Listener(observer))
  }

  inner class Listener(private val observer: Observer<in AutocompletePrediction>?) : MainThreadDisposable() {
    init {
      geoDataClient
          .getAutocompletePredictions(
              query,
              LatLngBounds.builder()
                  .include(LatLng(location.latitude + range, location.longitude + range))
                  .include(LatLng(location.latitude + range, location.longitude - range))
                  .include(LatLng(location.latitude - range, location.longitude + range))
                  .include(LatLng(location.latitude - range, location.longitude - range))
                  .build(),
              AutocompleteFilter.Builder()
                  .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                  .build()
          )
          .addOnSuccessListener {
            it.forEach {
              if (!isDisposed) {
                observer?.onNext(it)
              }
            }
          }
          .addOnFailureListener {
            if (!isDisposed) {
              observer?.onError(it)
            }
          }
          .addOnCompleteListener {
            observer?.onComplete()
          }
    }

    override fun onDispose() {}
  }
}