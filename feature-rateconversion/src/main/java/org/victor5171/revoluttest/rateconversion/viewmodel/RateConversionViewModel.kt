package org.victor5171.revoluttest.rateconversion.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.victor5171.revoluttest.repository.DispatchersContainer
import org.victor5171.revoluttest.repository.rateconversion.RateConversionRepository
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

private const val CURRENCY_UPDATE_RATE_IN_MILLISECONDS = 1000L
private const val DEFAULT_BASE_CURRENCY = "EUR"
private const val DEFAULT_STARTING_VALUE = 1.0f

class RateConversionViewModel @Inject constructor(
    private val locale: Locale,
    private val rateConversionRepository: RateConversionRepository,
    private val dispatchersContainer: DispatchersContainer
) : ViewModel() {
    // Create a empty MutableLiveData, just to avoid working with nullable values
    private var currentSource: LiveData<List<ConvertedRate>> = MutableLiveData()
    private var currentBaseCurrency: String? = null

    private val ratesMediatorLiveData = MediatorLiveData<List<ConvertedRate>>()
    private val currencyCache = mutableMapOf<String, Currency>()

    private var ratesUpdateJob: Job? = null

    val rates: LiveData<List<ConvertedRate>> = ratesMediatorLiveData

    init {
        convert(
            DEFAULT_BASE_CURRENCY,
            DEFAULT_STARTING_VALUE
        )
    }

    fun convert(baseCurrency: String, value: Float) {
        ratesMediatorLiveData.removeSource(currentSource)

        currentSource = createCurrencyConversionSource(baseCurrency, value)

        ratesMediatorLiveData.addSource(currentSource) {
            if (ratesMediatorLiveData.value != it) {
                ratesMediatorLiveData.value = it
            }
        }

        loadRates(baseCurrency)

        if (currentBaseCurrency != baseCurrency) {
            currentBaseCurrency = baseCurrency

            cancelUpdateTimer()
            startUpdateTimer(baseCurrency)
        }
    }

    private fun convertCurrencyValue(value: Float, multiplier: Float) = value * multiplier

    private fun createConvertedRate(currencyIdentifier: String, value: Float): ConvertedRate {
        val currency = currencyCache[currencyIdentifier] ?: Currency.getInstance(currencyIdentifier)
        val currencyName = currency.getDisplayName(locale)
        return ConvertedRate(currencyIdentifier, currencyName, value)
    }

    private fun createCurrencyConversionSource(
        baseCurrency: String,
        value: Float
    ): LiveData<List<ConvertedRate>> {
        return rateConversionRepository.getRatesByAscOrdering(baseCurrency)
            .map { rates ->
                val baseConvertedCurrency = createConvertedRate(baseCurrency, value)

                val convertedRates = rates.map {
                    createConvertedRate(
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
        ratesUpdateJob = viewModelScope.launch {
            while (isActive) {
                delay(CURRENCY_UPDATE_RATE_IN_MILLISECONDS)
                if (isActive) {
                    loadRates(baseCurrency)
                }
            }
        }
    }

    private fun cancelUpdateTimer() = ratesUpdateJob?.cancel()

    override fun onCleared() {
        cancelUpdateTimer()
    }

    @VisibleForTesting
    fun close() {
        onCleared()
    }
}
