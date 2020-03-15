package org.victor5171.revoluttest.app.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import org.victor5171.revoluttest.api.retrofit.RetrofitApiModule
import org.victor5171.revoluttest.api.retrofit.di.BaseUrl
import org.victor5171.revoluttest.app.MainActivity
import org.victor5171.revoluttest.persistence.room.di.RoomPersistenceModule
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionModule

@Singleton
@Component(
    modules = [
        ViewModelFactoryModule::class,
        RoomPersistenceModule::class,
        RetrofitApiModule::class,
        FeatureRateConversionModule::class,
        DispatchersContainerModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance @BaseUrl baseUrl: String
        ): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}
