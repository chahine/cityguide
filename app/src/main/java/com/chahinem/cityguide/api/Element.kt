package com.chahinem.cityguide.api

import com.squareup.moshi.Json

data class Element(

    @Json(name = "duration")
    val duration: Duration? = null,

    @Json(name = "distance")
    val distance: Distance? = null,

    @Json(name = "status")
    val status: String? = null
)
