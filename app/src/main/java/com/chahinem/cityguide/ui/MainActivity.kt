package com.chahinem.cityguide.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chahinem.cityguide.BuildConfig
import com.chahinem.cityguide.R.layout
import com.chahinem.cityguide.api.DistanceMatrixApi
import com.chahinem.cityguide.api.DistanceMatrixResponse
import com.chahinem.cityguide.repositories.LastLocationRepo
import com.chahinem.cityguide.ui.MainAdapter.MainItem
import com.chahinem.cityguide.utils.AutocompletePredictionsObservable
import com.chahinem.cityguide.utils.PlaceByIdObservable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.list
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject lateinit var distanceMatrixApi: DistanceMatrixApi
  @Inject lateinit var lastLocationRepo: LastLocationRepo

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var geoDataClient: GeoDataClient

  private val mainAdapter = MainAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    injectSelf()

    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    geoDataClient = Places.getGeoDataClient(this, null)

    list.layoutManager = LinearLayoutManager(this)
    list.adapter = mainAdapter
  }

  override fun onResume() {
    super.onResume()
    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION).let {
      if (it != PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION),
            RC_LOCATION)
      } else {
        fetchLastLocation()
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    when (requestCode) {
      RC_LOCATION -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
          fetchLastLocation()
        } else {
          // TODO: show why we needed permission
        }
        return
      }
    }
  }

  private fun injectSelf() {
    DaggerActivityComponent.builder()
        .activity(this)
        .app(application)
        .build()
        .inject(this)
  }

  private fun fetchLastLocation() {
    lastLocationRepo
        .lastLocation()
        .publish { shared ->
          Observable.mergeDelayError(
              shared.compose(placeComposer("bar")),
              shared.compose(placeComposer("bistro")),
              shared.compose(placeComposer("cafe"))
          )
        }
        .toList()
        .subscribe(
            { mainAdapter.swapData(it.filter { it.first == "bar" }.flatMap { it.second }) },
            { Timber.e(it) }
        )
  }

  private fun placeComposer(query: String): ObservableTransformer<in Location, out Pair<String, List<MainItem>>> {
    return ObservableTransformer { upstream ->
      upstream
          .switchMapSingle { location ->
            AutocompletePredictionsObservable(this, query, location)
                .concatMap { fetchDistanceMatrix(location, it) }
                .map { (a, b) -> MainItem(a, b) }
                .toList()
                .map { query to it }
          }
    }
  }

  private fun fetchDistanceMatrix(location: Location,
                                  prediction: AutocompletePrediction
  ): Observable<Pair<Place, DistanceMatrixResponse>>? {
    return Observable.zip(
        PlaceByIdObservable(this, prediction.placeId),
        distanceMatrixApi
            .distance(
                "imperial",
                "${location.latitude},${location.longitude}",
                "place_id:${prediction.placeId}",
                BuildConfig.GOOGLE_DISTANCE_API_KEY
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()),
        BiFunction { a: Place, b: DistanceMatrixResponse -> a to b }
    )
  }

  companion object {
    private const val RC_LOCATION = 42
  }
}
