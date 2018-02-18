package com.chahinem.cityguide.repositories

import android.app.Activity
import android.util.LruCache
import com.chahinem.cityguide.utils.PlaceByIdObservable
import com.google.android.gms.location.places.Place
import io.reactivex.Observable
import javax.inject.Inject

class PlaceRepo @Inject constructor(private val activity: Activity) {

  private val cache = LruCache<String, Place>(20)

  fun placeById(placeId: String): Observable<Place> {
    val place = cache.get(placeId)
    return if (place == null)
      PlaceByIdObservable(activity, placeId).doOnNext { cache.put(it.id, it) }
    else {
      Observable.just(place)
    }
  }
}
