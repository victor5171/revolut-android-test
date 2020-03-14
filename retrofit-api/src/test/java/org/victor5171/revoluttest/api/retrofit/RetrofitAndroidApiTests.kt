package org.victor5171.revoluttest.api.retrofit

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException

class RetrofitAndroidApiTests {

    @Test
    fun `When I try to get the latest rates with a correct base currency, it should work`() =
        runBlocking {
            testApiWithUrl {
                val apiBuilder = ApiBuilder(it)

                val latestRatesResponse = apiBuilder.androidApi.getLatestRates("EUR")

                Assert.assertTrue(latestRatesResponse.baseCurrency.isNotEmpty())
                Assert.assertTrue(latestRatesResponse.rates.isNotEmpty())
            }
        }

    @Test(expected = HttpException::class)
    fun `When I try to get the latest with an invalid base currency, it should throw an exception`() =
        runBlocking {
            testApiWithUrl {
                val apiBuilder = ApiBuilder(it)

                apiBuilder.androidApi.getLatestRates("INVALID")
            }
        }
}
