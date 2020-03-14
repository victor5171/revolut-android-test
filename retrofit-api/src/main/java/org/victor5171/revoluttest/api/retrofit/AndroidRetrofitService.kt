package org.victor5171.revoluttest.api.retrofit

import org.victor5171.revoluttest.api.LatestRatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AndroidRetrofitService {
    @GET("android/latest")
    suspend fun getLatestRates(@Query("base") baseCurrency: String): LatestRatesResponse
}
