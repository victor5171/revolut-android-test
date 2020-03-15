package org.victor5171.revoluttest.rateconversion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.victor5171.revoluttest.TestDispatchersContainer
import org.victor5171.revoluttest.UnconfinedTestDispatchersContainer
import org.victor5171.revoluttest.repository.rateconversion.Rate
import org.victor5171.revoluttest.repository.rateconversion.RateConversionRepository

@FlowPreview
@ExperimentalCoroutinesApi
class RateConversionViewModelRatesTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When I open the view model and no destination currency is loaded, it should only return the base currency on the rates LiveData`() {
        val baseCurrency = "EUR"

        val repository = mockk<RateConversionRepository> {
            every { getRatesByAscOrdering(baseCurrency) } returns flowOf(emptyList())
            coEvery { loadRates(baseCurrency) } returns Unit
        }

        val viewModel = RateConversionViewModel(repository, UnconfinedTestDispatchersContainer)

        viewModel.rates.test().assertValue {
            it.size == 1 && it.contains(ConvertedRate(baseCurrency, 1.0f))
        }
    }

    @Test
    fun `When I open the view model and no destination currency is loaded, but after 5 seconds they are loaded, it should update the rates LiveData`() {
        val baseCurrency = "EUR"

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val ratesFlow: Flow<List<Rate>> = flow {
            emit(emptyList())
            delay(5000L)
            emit(
                listOf(
                    Rate("USD", 0.9f)
                )
            )
        }.flowOn(testCoroutineDispatcher)

        val repository = mockk<RateConversionRepository> {
            every { getRatesByAscOrdering(baseCurrency) } returns ratesFlow
            coEvery { loadRates(baseCurrency) } returns Unit
        }

        val viewModel =
            RateConversionViewModel(repository, TestDispatchersContainer(testCoroutineDispatcher))

        val ratesTestObserver = viewModel.rates.test()

        ratesTestObserver.assertValue {
            it.size == 1 && it.contains(ConvertedRate(baseCurrency, 1.0f))
        }

        testCoroutineDispatcher.advanceTimeBy(5000L)

        ratesTestObserver.assertValue {
            it.size == 2
                && it.contains(ConvertedRate(baseCurrency, 1.0f))
                && it.contains(ConvertedRate("USD", 0.9f))
        }
    }

    @Test
    fun `When I change the base currency to be converted, the old base currency emissions shouldnt be emitted on the rates LiveData`() {
        val baseCurrency = "EUR"
        val dolarCurrency = "USD"

        val channelForBaseCurrency = ConflatedBroadcastChannel<List<Rate>>()

        val repository = mockk<RateConversionRepository> {
            every { getRatesByAscOrdering(baseCurrency) } returns channelForBaseCurrency.asFlow()
            every { getRatesByAscOrdering(dolarCurrency) } returns flowOf(emptyList())
            coEvery { loadRates(or(baseCurrency, dolarCurrency)) } returns Unit
        }

        val viewModel = RateConversionViewModel(repository, UnconfinedTestDispatchersContainer)

        channelForBaseCurrency.sendBlocking(emptyList())

        val ratesTestObserver = viewModel.rates.test()

        ratesTestObserver.assertValue {
            it.size == 1 && it.contains(ConvertedRate(baseCurrency, 1.0f))
        }

        viewModel.convert(dolarCurrency, 1.0f)

        ratesTestObserver.assertValue {
            it.size == 1 && it.contains(ConvertedRate(dolarCurrency, 1.0f))
        }

        // This should be ignored, because the view model is observing another source since the base
        // currency has changed
        channelForBaseCurrency.sendBlocking(listOf(
            Rate("USD", 0.9f)
        ))

        //The LiveData should maintain the same value as before
        ratesTestObserver.assertValue {
            it.size == 1 && it.contains(ConvertedRate(dolarCurrency, 1.0f))
        }

        //Asserting the history of values
        ratesTestObserver.assertValueHistory(
            listOf(ConvertedRate(baseCurrency, 1.0f)),
            listOf(ConvertedRate(dolarCurrency, 1.0f))
        )
    }

    @Test
    fun `When I change the value to be converted, but not the base currency, the emissions should use the same source as before, but the converted values should be updated`() {
        val baseCurrency = "EUR"

        val channelForBaseCurrency = ConflatedBroadcastChannel<List<Rate>>()

        val repository = mockk<RateConversionRepository> {
            every { getRatesByAscOrdering(baseCurrency) } returns channelForBaseCurrency.asFlow()
            coEvery { loadRates(baseCurrency) } returns Unit
        }

        val viewModel = RateConversionViewModel(repository, UnconfinedTestDispatchersContainer)

        channelForBaseCurrency.sendBlocking(listOf(
            Rate("USD", 0.9f)
        ))

        val ratesTestObserver = viewModel.rates.test()

        ratesTestObserver.assertValue {
            it.size == 2
                && it.contains(ConvertedRate(baseCurrency, 1.0f))
                && it.contains(ConvertedRate("USD", 0.9f))
        }

        viewModel.convert(baseCurrency, 2.0f)

        //Using the same rates, but checking if the converted values were updated
        ratesTestObserver.assertValue {
            it.size == 2
                && it.contains(ConvertedRate(baseCurrency, 2.0f))
                && it.contains(ConvertedRate("USD", 1.8f))
        }

        //Updating the rates
        channelForBaseCurrency.sendBlocking(listOf(
            Rate("USD", 0.8f)
        ))

        //Using the same rates, but checking if the converted values were updated
        ratesTestObserver.assertValue {
            it.size == 2
                && it.contains(ConvertedRate(baseCurrency, 2.0f))
                && it.contains(ConvertedRate("USD", 1.6f))
        }

        ratesTestObserver.assertValueHistory(
            listOf(ConvertedRate(baseCurrency, 1.0f), ConvertedRate("USD", 0.9f)),
            listOf(ConvertedRate(baseCurrency, 2.0f), ConvertedRate("USD", 1.8f)),
            listOf(ConvertedRate(baseCurrency, 2.0f), ConvertedRate("USD", 1.6f))
        )
    }
}