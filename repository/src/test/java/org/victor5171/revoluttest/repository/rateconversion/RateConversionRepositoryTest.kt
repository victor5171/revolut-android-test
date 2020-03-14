package org.victor5171.revoluttest.repository.rateconversion

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.victor5171.revoluttest.api.AndroidApi
import org.victor5171.revoluttest.api.LatestRatesResponse
import org.victor5171.revoluttest.persistence.rate.RateDAO
import org.victor5171.revoluttest.persistence.rate.RateDTO

class RateConversionRepositoryTest {
    @Test
    fun `When I get an emission on the rates source, it should emit a Rate object`() = runBlocking {
        val baseCurrency = "TES"
        val rateDTO = RateDTO("DES", 1.1f)

        val rateDAO = mockk<RateDAO> {
            every { getRatesByAscOrdering(baseCurrency) } returns flowOf(listOf(rateDTO))
        }

        val repository = RateConversionRepository(rateDAO, mockk())

        val rates = repository.getRatesByAscOrdering(baseCurrency).first()

        Assert.assertEquals(1, rates.size)
        Assert.assertEquals(rateDTO.destinationCurrency, rates[0].destinationCurrency)
        Assert.assertEquals(rateDTO.multiplier, rates[0].multiplier)
    }

    @Test(expected = Exception::class)
    fun `When an error happens on the rates source, this error should be propagated upwards`() =
        runBlocking {
            val baseCurrency = "TES"

            val rateDAO = mockk<RateDAO> {
                every { getRatesByAscOrdering(baseCurrency) } throws Exception()
            }

            val repository = RateConversionRepository(rateDAO, mockk())

            repository.getRatesByAscOrdering(baseCurrency).first()

            Unit
        }

    @Test
    fun `When I try to load rates and no error happens, they should be updates on the database`() =
        runBlocking {
            val baseCurrency = "TES"

            val latestRatesResponse = LatestRatesResponse(baseCurrency, mapOf("DES" to 1.1f))

            val androidApi = mockk<AndroidApi> {
                coEvery { getLatestRates(baseCurrency) } returns latestRatesResponse
            }

            val ratesDTOsSlot = slot<List<RateDTO>>()

            val rateDAO = mockk<RateDAO> {
                coEvery { updateRates(baseCurrency, capture(ratesDTOsSlot)) } returns Unit
            }

            val repository = RateConversionRepository(rateDAO, androidApi)
            repository.loadRates(baseCurrency)

            coVerify(exactly = 1) { rateDAO.updateRates(baseCurrency, any()) }

            Assert.assertTrue(ratesDTOsSlot.isCaptured)

            val capturedRatesDTOs = ratesDTOsSlot.captured
            Assert.assertEquals(1, capturedRatesDTOs.size)
            Assert.assertEquals("DES", capturedRatesDTOs[0].destinationCurrency)
            Assert.assertEquals(1.1f, capturedRatesDTOs[0].multiplier)
        }

    @Test(expected = Exception::class)
    fun `When I try to load rates and a error happens while loading, they shouldn't be updated on the database`() =
        runBlocking {
            val baseCurrency = "TES"

            val androidApi = mockk<AndroidApi> {
                coEvery { getLatestRates(baseCurrency) } throws Exception()
            }

            val rateDAO = mockk<RateDAO>()

            val repository = RateConversionRepository(rateDAO, androidApi)
            repository.loadRates(baseCurrency)

            coVerify(exactly = 0) { rateDAO.updateRates(baseCurrency, any()) }
        }
}
