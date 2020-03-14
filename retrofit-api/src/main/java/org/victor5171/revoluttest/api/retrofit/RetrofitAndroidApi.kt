package org.victor5171.revoluttest.api.retrofit

import org.victor5171.revoluttest.api.AndroidApi
import org.victor5171.revoluttest.api.LatestRatesResponse

class RetrofitAndroidApi internal constructor(
    private val androidRetrofitService: AndroidRetrofitService
) : AndroidApi {

    override suspend fun getLatestRates(baseCurrency: String): LatestRatesResponse {
        return androidRetrofitService.getLatestRates(baseCurrency)
    }
}
