package org.victor5171.revoluttest.app.di

import android.app.Application
import android.os.Build
import dagger.Module
import dagger.Provides
import org.victor5171.revoluttest.rateconversion.di.DecimalSeparator
import org.victor5171.revoluttest.repository.DispatchersContainer
import org.victor5171.revoluttest.repository.RealDispatchersContainer
import java.text.DecimalFormat
import java.util.Locale
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun providesRealDispatcherContainer(): DispatchersContainer {
        return RealDispatchersContainer
    }

    @Provides
    @Singleton
    fun providesLocale(application: Application): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return application.resources.configuration.locales[0]
        }

        @Suppress("DEPRECATION")
        return application.resources.configuration.locale
    }

    @Provides
    @Singleton
    fun providesDecimalFormat(locale: Locale): DecimalFormat {
        val decimalFormat =  DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
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
