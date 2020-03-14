package org.victor5171.revoluttest.api

interface AndroidApi {
    suspend fun getLatestRates(baseCurrency: String): LatestRatesResponse
}
