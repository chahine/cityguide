package com.chahinem.cityguide.api

import com.squareup.moshi.Json

data class Row(

    @Json(name = "elements")
    val elements: List<Element>? = null
)