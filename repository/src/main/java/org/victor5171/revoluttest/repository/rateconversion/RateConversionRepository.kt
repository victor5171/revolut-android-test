package org.victor5171.revoluttest.repository.rateconversion

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.victor5171.revoluttest.api.AndroidApi
import org.victor5171.revoluttest.persistence.rate.RateDAO
import org.victor5171.revoluttest.persistence.rate.RateDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateConversionRepository @Inject constructor(
    private val rateDAO: RateDAO,
    private val androidApi: AndroidApi
) {
    fun getRatesByAscOrdering(baseCurrency: String): Flow<List<Rate>> {
        return rateDAO.getRatesByAscOrdering(baseCurrency)
            .map { rates ->
                rates.map { Rate(it.destinationCurrency, it.multiplier) }
            }
    }

    suspend fun loadRates(baseCurrency: String) {
        val latestRatesResponse = androidApi.getLatestRates(baseCurrency)

        val ratesMap = latestRatesResponse.rates

        val ratesDTOs = ratesMap.map { (currency, multiplier) ->
            RateDTO(currency, multiplier)
        }

        rateDAO.updateRates(baseCurrency, ratesDTOs)
    }
}
