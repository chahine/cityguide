package com.chahinem.cityguide.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DistanceMatrixApi {
  @GET("distancematrix/json")
  fun distance(
      @Query("units") units: String,
      @Query("origins") origins: String,
      @Query("destinations") destinations: String,
      @Query("key") key: String
  ): Observable<DistanceMatrixResponse>
}
