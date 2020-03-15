package org.victor5171.revoluttest.persistence.room.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.victor5171.revoluttest.persistence.rate.RateDAO
import org.victor5171.revoluttest.persistence.room.AppDatabase
import javax.inject.Singleton

private const val APP_DATABASE_FILE_NAME = "appDatabase"

@Module
class RoomPersistenceModule {

    private var appDatabase: AppDatabase? = null

    /**
     * Unfortunately I have to call this method every time, because this is the only way I can build
     * databases, requiring the bound Application parameter
     */
    private fun buildAppDatabase(application: Application): AppDatabase {
        appDatabase?.let { return it }

        return Room.databaseBuilder(application, AppDatabase::class.java, APP_DATABASE_FILE_NAME)
            .build().also {
                appDatabase = it
            }
    }

    @Singleton
    @Provides
    fun providesRateDAO(application: Application): RateDAO {
        return buildAppDatabase(application).rateDao()
    }
}
