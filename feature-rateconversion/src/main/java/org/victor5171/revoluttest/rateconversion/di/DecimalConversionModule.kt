package org.victor5171.revoluttest.rateconversion.di

import dagger.Module
import dagger.Provides
import java.text.DecimalFormat
import java.util.Locale
import javax.inject.Singleton

@Module
class DecimalConversionModule {
    @Provides
    @Singleton
    fun providesDecimalFormat(locale: Locale): DecimalFormat {
        val decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        decimalFormat.decimalFormatSymbols = decimalFormat.decimalFormatSymbols.apply {
            currencySymbol = ""
        }

        return decimalFormat
    }

    @Provides
    @DecimalSeparator
    @Singleton
    fun providesDecimalSeparator(decimalFormat: DecimalFormat): Char? {
        return decimalFormat.decimalFormatSymbols.monetaryDecimalSeparator
    }
}
