package org.victor5171.revoluttest.app.di

import dagger.Module
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionSubComponent

@Module(subcomponents = [
    FeatureRateConversionSubComponent::class
])
interface AppSubcomponents
