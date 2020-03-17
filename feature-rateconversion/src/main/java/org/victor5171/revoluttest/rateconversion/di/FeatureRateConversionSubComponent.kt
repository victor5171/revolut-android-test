package org.victor5171.revoluttest.rateconversion.di

import dagger.Subcomponent
import org.victor5171.revoluttest.rateconversion.ui.RatesConversionFragment

@Subcomponent(modules = [FeatureRateConversionModule::class])
interface FeatureRateConversionSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FeatureRateConversionSubComponent
    }

    fun inject(ratesConversionFragment: RatesConversionFragment)
}
