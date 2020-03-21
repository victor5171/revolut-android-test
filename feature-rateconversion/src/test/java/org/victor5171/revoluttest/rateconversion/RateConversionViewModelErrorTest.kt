package org.victor5171.revoluttest.rateconversion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.victor5171.revoluttest.UnconfinedTestDispatchersContainer
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel
import org.victor5171.revoluttest.repository.rateconversion.RateConversionRepository
import java.util.Locale

class RateConversionViewModelErrorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When an error happens while loading the rates, it shouldn't trigger an exception`() {
        val baseCurrency = "EUR"

        val mockedException = Exception()

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } throws mockedException
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val viewModel = RateConversionViewModel(
            Locale.getDefault(),
            repository,
            UnconfinedTestDispatchersContainer
        )

        viewModel.errorLoadingData.test().assertValue { it == mockedException }
    }

    @Test
    fun `When an error happens and then it works, it should post the exception and then null on the error loading livedata`() {
        val baseCurrency = "EUR"

        val mockedException = Exception()

        val repository = mockk<RateConversionRepository> {
            coEvery { loadRates(baseCurrency) } throws mockedException andThen Unit
            every { getRatesByAscOrdering(baseCurrency) } returns emptyFlow()
        }

        val viewModel = RateConversionViewModel(
            Locale.getDefault(),
            repository,
            UnconfinedTestDispatchersContainer
        )

        val testObserver = viewModel.errorLoadingData.test()

        testObserver.assertValue { it == mockedException }

        viewModel.convert(baseCurrency, 1f)

        testObserver.assertValue { it == null }
    }
}