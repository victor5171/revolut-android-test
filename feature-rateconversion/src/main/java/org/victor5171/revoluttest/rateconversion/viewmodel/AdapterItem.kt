package org.victor5171.revoluttest.rateconversion.viewmodel

sealed class AdapterItem

data class ConvertedRate(
    val currencyIdentifier: String,
    val currencyName: String,
    val value: Double?
) : AdapterItem()

object LoadingItem : AdapterItem()
