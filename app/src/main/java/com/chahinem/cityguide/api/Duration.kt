package com.chahinem.cityguide.api

import com.squareup.moshi.Json

data class Duration(

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "value")
    val value: Int? = null
)
