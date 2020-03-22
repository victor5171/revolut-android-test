package org.victor5171.revoluttest.rateconversion.di

import android.icu.text.DecimalFormat
import dagger.Module
import dagger.Provides
import java.util.Locale
import javax.inject.Singleton

@Module
class DecimalConversionModule {

    @Provides
    @Singleton
    fun providesDecimalFormat(locale: Locale): DecimalFormat {
        return DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
    }
}
