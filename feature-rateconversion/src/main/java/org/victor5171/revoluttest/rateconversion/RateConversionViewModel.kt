package org.victor5171.revoluttest.rateconversion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.victor5171.revoluttest.repository.DispatchersContainer
import org.victor5171.revoluttest.repository.rateconversion.RateConversionRepository
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

private const val CURRENCY_UPDATE_RATE_IN_MILLISECONDS = 1000L

class RateConversionViewModel(
    private val rateConversionRepository: RateConversionRepository,
    private val dispatchersContainer: DispatchersContainer
) : ViewModel() {
    //Create a empty MutableLiveData, just to avoid working with nullable values
    private var currentSource: LiveData<List<ConvertedRate>> = MutableLiveData()

    private val ratesMediatorLiveData = MediatorLiveData<List<ConvertedRate>>()

    private lateinit var ratesUpdateTimer: Timer

    val rates: LiveData<List<ConvertedRate>> = ratesMediatorLiveData

    fun convert(baseCurrency: String, value: Float) {
        ratesMediatorLiveData.removeSource(currentSource)

        currentSource = createCurrencyConversionSource(baseCurrency, value)

        ratesMediatorLiveData.addSource(currentSource) {
            ratesMediatorLiveData.value = it
        }

        loadRates(baseCurrency)

        cancelUpdateTimer()
        startUpdateTimer(baseCurrency)
    }

    private fun convertCurrencyValue(value: Float, multiplier: Float) = value * multiplier

    private fun createCurrencyConversionSource(
        baseCurrency: String,
        value: Float
    ): LiveData<List<ConvertedRate>> {
        return rateConversionRepository.getRatesByAscOrdering(baseCurrency)
            .map { rates ->
                val baseConvertedCurrency = ConvertedRate(baseCurrency, value)

                val convertedRates = rates.map {
                    ConvertedRate(
                        it.destinationCurrency,
                        convertCurrencyValue(value, it.multiplier)
                    )
                }

                listOf(baseConvertedCurrency, *convertedRates.toTypedArray())
            }
            .asLiveData()
    }

    private fun loadRates(baseCurrency: String) {
        viewModelScope.launch {
            withContext(dispatchersContainer.io) {
                rateConversionRepository.loadRates(baseCurrency)
            }
        }
    }

    private fun startUpdateTimer(baseCurrency: String) {
        ratesUpdateTimer = fixedRateTimer(period = CURRENCY_UPDATE_RATE_IN_MILLISECONDS) {
            loadRates(baseCurrency)
        }
    }

    private fun cancelUpdateTimer() = ratesUpdateTimer.cancel()

    override fun onCleared() {
        cancelUpdateTimer()
    }
}