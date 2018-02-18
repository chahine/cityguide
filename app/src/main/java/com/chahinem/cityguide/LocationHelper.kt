package com.chahinem.cityguide

import android.annotation.SuppressLint
import android.app.Activity
import com.chahinem.cityguide.MainAdapter.MainItem
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import timber.log.Timber

@SuppressLint("MissingPermission")
class LocationHelper(activity: Activity) {

  private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
  private val geoDataClient = Places.getGeoDataClient(activity, null)

  init {
    fusedLocationClient.lastLocation.addOnSuccessListener(activity, { location ->
      if (location != null) {
        Timber.d("--> lastLocation: $location")
        geoDataClient
            .getAutocompletePredictions(
                "cafe",
                LatLngBounds(
                    LatLng(location.latitude - 1, location.latitude - 1),
                    LatLng(location.latitude + 1, location.latitude + 1)),
                AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build()
            )
            .addOnCompleteListener({
              val result = it.result
              val data = mutableListOf<MainItem>()
            })
      }
    })
  }
}