package com.chahinem.cityguide.ui

import android.app.Activity
import android.location.Location
import com.chahinem.cityguide.BuildConfig
import com.chahinem.cityguide.api.DistanceMatrixApi
import com.chahinem.cityguide.api.DistanceMatrixResponse
import com.chahinem.cityguide.repositories.LastLocationRepo
import com.chahinem.cityguide.repositories.PlaceRepo
import com.chahinem.cityguide.ui.MainEvent.LoadMain
import com.chahinem.cityguide.ui.MainModel.MainFailure
import com.chahinem.cityguide.ui.MainModel.MainProgress
import com.chahinem.cityguide.ui.MainModel.MainSuccess
import com.chahinem.cityguide.utils.AutocompletePredictionsObservable
import com.google.android.gms.location.places.Place
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val activity: Activity,
    private val placeRepo: PlaceRepo,
    private val distanceMatrixApi: DistanceMatrixApi,
    private val lastLocationRepo: LastLocationRepo) {

  fun places(): ObservableTransformer<in LoadMain, out MainModel> {
    return ObservableTransformer {
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
          .flatMapObservable { places ->
            fetchDistanceMatrix(places.map { it.second.id }.toList())
                .map {
                  val mainItems = mutableListOf<MainAdapter.Item>()
                  places.forEachIndexed { index, pair ->
                    mainItems.add(MainAdapter.Item(
                        pair.first,
                        pair.second,
                        it.rows?.get(0)?.elements?.get(index)?.distance?.text))
                  }
                  mainItems
                }
          }
          .map { MainSuccess(it) as MainModel }
          .onErrorReturn { MainFailure(it) }
          .startWith(MainProgress())
    }
  }

  private fun placeComposer(query: String): ObservableTransformer<in Location, out Pair<String, Place>> {
    return ObservableTransformer {
      it.switchMap { location ->
        AutocompletePredictionsObservable(activity, query, location)
            .flatMap { placeRepo.placeById(it.placeId.orEmpty()) }
            .map { query to it }
      }
    }
  }

  private fun fetchDistanceMatrix(placesIds: List<String>): Observable<DistanceMatrixResponse> {
    val origins = placesIds
        .map { "place_id:$it" }
        .foldRight("", { a, b -> "$a|$b" })

    return lastLocationRepo
        .lastLocation()
        .concatMap {
          distanceMatrixApi
              .distance(
                  "imperial",
                  "${it.latitude},${it.longitude}",
                  origins,
                  BuildConfig.GOOGLE_DISTANCE_API_KEY
              )
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
        }
  }
}
