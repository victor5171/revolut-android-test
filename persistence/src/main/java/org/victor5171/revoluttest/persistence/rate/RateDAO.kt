package org.victor5171.revoluttest.persistence.rate

import kotlinx.coroutines.flow.Flow

interface RateDAO {
    suspend fun updateRates(baseCurrency: String, rates: List<RateDTO>)
    fun getRatesByAscOrdering(baseCurrency: String): Flow<List<RateDTO>>
}
