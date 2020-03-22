package org.victor5171.revoluttest.rateconversion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.victor5171.revoluttest.TestDispatchersContainer
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel
import org.victor5171.revoluttest.repository.DispatchersContainer
import org.victor5171.revoluttest.repository.rateconversion.RateConversionRepository

@ExperimentalCoroutinesApi
class RateConversionViewModelScheduleTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `When I open the view model and wait for some time, it should update the rates every 1 second`() {
        val baseCurrency = "EUR"

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } returns Unit
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val dispatchersContainer = TestDispatchersContainer(testCoroutineDispatcher)

        Dispatchers.setMain(testCoroutineDispatcher)

        RateConversionViewModel(
            Locale.getDefault(),
            repository,
            dispatchersContainer
        )

        testCoroutineDispatcher.advanceTimeBy(5000L)

        coVerify(exactly = 6) { repository.loadRates(baseCurrency) }
    }

    @Test
    fun `When I open the view model and an error happens while loading, it should continue trying every 1 second`() {
        val baseCurrency = "EUR"

        val mockedException = Exception()

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } throws mockedException andThen Unit
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val dispatchersContainer = TestDispatchersContainer(testCoroutineDispatcher)

        Dispatchers.setMain(testCoroutineDispatcher)

        val viewModel = RateConversionViewModel(
            Locale.getDefault(),
            repository,
            dispatchersContainer
        )

        val testErrorObserver = viewModel.errorLoadingData.test()

        testCoroutineDispatcher.advanceTimeBy(1000L)

        coVerify(exactly = 2) { repository.loadRates(baseCurrency) }

        testErrorObserver.assertValueHistory(mockedException, null)
    }

    @Test
    fun `When I open the view model and then change the currency, it should cancel and reschedule the currency update`() {
        val euroCurrency = "EUR"
        val dolarCurrency = "USD"

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(or(euroCurrency, dolarCurrency)) } returns Unit
            every { getRatesByAscOrdering(or(euroCurrency, dolarCurrency)) } returns emptyFlow()
        }

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val dispatchersContainer = TestDispatchersContainer(testCoroutineDispatcher)

        Dispatchers.setMain(testCoroutineDispatcher)

        val viewModel =
            RateConversionViewModel(
                Locale.getDefault(),
                repository,
                dispatchersContainer
            )

        coVerify(exactly = 1) { repository.loadRates(euroCurrency) }

        // Wait a small amount of time, not enough to update the rates
        testCoroutineDispatcher.advanceTimeBy(500L)

        coVerify(exactly = 1) { repository.loadRates(euroCurrency) }

        // Change to a new currency, should reset the timer
        viewModel.convert(dolarCurrency, 1.0)

        // Wait a small amount of time, not enough to update the rates
        testCoroutineDispatcher.advanceTimeBy(500L)

        // No timer should be executed yet
        coVerify(exactly = 1) { repository.loadRates(euroCurrency) }
        coVerify(exactly = 1) { repository.loadRates(dolarCurrency) }

        // Wait more half a second, so the timer can update the newly set base currency
        testCoroutineDispatcher.advanceTimeBy(500L)

        coVerify(exactly = 1) { repository.loadRates(euroCurrency) }
        coVerify(exactly = 2) { repository.loadRates(dolarCurrency) }
    }

    @Test
    fun `When I open the view model, wait a little bit, update the convert value, but keep the same currency, it shouldnt resschedule the update`() {
        val baseCurrency = "EUR"

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } returns Unit
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val dispatchersContainer = object : DispatchersContainer {
            override val io = testCoroutineDispatcher
            override val main = testCoroutineDispatcher
        }

        Dispatchers.setMain(testCoroutineDispatcher)

        val viewModel =
            RateConversionViewModel(
                Locale.getDefault(),
                repository,
                dispatchersContainer
            )

        testCoroutineDispatcher.advanceTimeBy(500L)

        coVerify(exactly = 1) { repository.loadRates(baseCurrency) }

        viewModel.convert(baseCurrency, 1.2)

        coVerify(exactly = 2) { repository.loadRates(baseCurrency) }

        testCoroutineDispatcher.advanceTimeBy(500L)

        coVerify(exactly = 3) { repository.loadRates(baseCurrency) }
    }

    @Test
    fun `When I open the view model, but close it and wait for the auto update time, it shouldnt update, cancelling all timers`() {
        val baseCurrency = "EUR"

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } returns Unit
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val testCoroutineDispatcher = TestCoroutineDispatcher()

        val dispatchersContainer = object : DispatchersContainer {
            override val io = testCoroutineDispatcher
            override val main = testCoroutineDispatcher
        }

        Dispatchers.setMain(testCoroutineDispatcher)

        val viewModel =
            RateConversionViewModel(
                Locale.getDefault(),
                repository,
                dispatchersContainer
            )

        coVerify(exactly = 1) { repository.loadRates(baseCurrency) }

        viewModel.close()

        testCoroutineDispatcher.advanceTimeBy(1000L)

        coVerify(exactly = 1) { repository.loadRates(baseCurrency) }
    }
}
