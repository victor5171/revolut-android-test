package org.victor5171.revoluttest.app

import android.app.Application
import org.victor5171.revoluttest.BuildConfig
import org.victor5171.revoluttest.app.di.DaggerAppComponent
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionSubComponent
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionSubComponentContainer

class MainApplication : Application(), FeatureRateConversionSubComponentContainer {
    private val appComponent = DaggerAppComponent.factory().create(this, BuildConfig.API_BASE_URL)

    override fun provideFeatureRateConversionSubComponent(): FeatureRateConversionSubComponent {
        return appComponent.featureRateConversionSubComponent().create()
    }
}
