package org.victor5171.revoluttest.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatestRatesResponse(
    val baseCurrency: String,
    val rates: Map<String, Float>
)
