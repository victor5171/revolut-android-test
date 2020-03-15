package org.victor5171.revoluttest.rateconversion.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.victor5171.revoluttest.rateconversion.RateConversionViewModel
import org.victor5171.viewmodelextensions.viewmodelfactory.ViewModelKey

@Module
interface FeatureRateConversionModule {
    @Binds
    @IntoMap
    @ViewModelKey(RateConversionViewModel::class)
    fun bindRateConversionViewModel(rateConversionViewModel: RateConversionViewModel): ViewModel
}
