package org.victor5171.revoluttest.rateconversion.di

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import dagger.Module
import dagger.Provides
import java.util.Locale
import javax.inject.Singleton

@Module
class DecimalConversionModule {

    @Provides
    @Singleton
    fun providesNumberFormat(locale: Locale): NumberFormat {
        return NumberFormat.getNumberInstance(locale)
    }

    @Provides
    @Singleton
    fun providesDecimalFormat(locale: Locale): DecimalFormat {
        val decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        decimalFormat.decimalFormatSymbols = decimalFormat.decimalFormatSymbols.apply {
            currencySymbol = ""
        }

        return decimalFormat
    }
}
