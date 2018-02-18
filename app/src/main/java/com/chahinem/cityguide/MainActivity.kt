package com.chahinem.cityguide

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chahinem.cityguide.MainAdapter.MainItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.activity_main.list
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var geoDataClient: GeoDataClient

  private val mainAdapter = MainAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    geoDataClient = Places.getGeoDataClient(this, null)

    list.layoutManager = LinearLayoutManager(this)
    list.adapter = mainAdapter
  }

  override fun onResume() {
    super.onResume()
    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION).let {
      if (it != PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), 42)
      } else {
        fetchLastLocation()
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    when (requestCode) {
      42 -> {
        // If request is cancelled, the result arrays are empty.
        if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
          fetchLastLocation()
        } else {
          // TODO: show why we needed permission
        }
        return
      }
    }
  }

  private fun fetchLastLocation() {
    LastLocationObservable(this)
        .switchMap { AutocompletePredictionsObservable(this, "bistro", it) }
        .concatMap { PlaceByIdObservable(this, it.placeId) }
        .map { MainItem(it) }
        .toList()
        .subscribe(
            { mainAdapter.swapData(it) },
            { Timber.e(it) }
        )
  }
}
