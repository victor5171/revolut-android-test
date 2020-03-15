package org.victor5171.revoluttest.app.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import org.victor5171.viewmodelextensions.viewmodelfactory.DaggerViewModelFactory

@Module
interface ViewModelFactoryModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}
