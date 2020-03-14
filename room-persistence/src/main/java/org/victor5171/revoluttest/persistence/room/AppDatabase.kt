package org.victor5171.revoluttest.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.victor5171.revoluttest.persistence.room.rate.RoomRateDAO
import org.victor5171.revoluttest.persistence.room.rate.RoomRateDTO

@Database(
    entities = [RoomRateDTO::class],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RoomRateDAO
}
