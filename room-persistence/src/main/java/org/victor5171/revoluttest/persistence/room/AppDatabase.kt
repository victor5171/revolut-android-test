package org.victor5171.revoluttest.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.victor5171.revoluttest.persistence.room.rate.RateDAO
import org.victor5171.revoluttest.persistence.room.rate.RateDTO

@Database(
    entities = [RateDTO::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDAO
}
