package org.victor5171.revoluttest.rateconversion.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.victor5171.revoluttest.rateconversion.numberformatter.AndroidIcuNumberFormatter
import org.victor5171.revoluttest.rateconversion.numberformatter.NumberFormatter
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.ui.keylistener.NumberKeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel
import org.victor5171.viewmodelextensions.viewmodelfactory.ViewModelKey

@Module
interface FeatureRateConversionModule {
    @Binds
    @IntoMap
    @ViewModelKey(RateConversionViewModel::class)
    fun bindRateConversionViewModel(rateConversionViewModel: RateConversionViewModel): ViewModel

    @Binds
    fun bindKeyListenerBuilder(numberKeyListenerBuilder: NumberKeyListenerBuilder): KeyListenerBuilder

    @Binds
    fun bindNumberFormatter(androidIcuNumberFormatter: AndroidIcuNumberFormatter): NumberFormatter
}
