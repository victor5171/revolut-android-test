package org.victor5171.revoluttest.app.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import org.victor5171.revoluttest.repository.DispatchersContainer
import org.victor5171.revoluttest.repository.RealDispatchersContainer

@Module
class DispatchersContainerModule {
    @Provides
    @Singleton
    fun providesRealDispatcherContainer(): DispatchersContainer {
        return RealDispatchersContainer
    }
}
