package com.chahinem.cityguide.api

import com.squareup.moshi.Json

data class DistanceMatrixResponse(

    @Json(name = "destination_addresses")
    val destinationAddresses: List<String?>? = null,

    @Json(name = "rows")
    val rows: List<Row>? = null,

    @Json(name = "origin_addresses")
    val originAddresses: List<String>? = null,

    @Json(name = "status")
    val status: String? = null
)
