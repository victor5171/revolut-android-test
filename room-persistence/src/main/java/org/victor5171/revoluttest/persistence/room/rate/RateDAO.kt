package org.victor5171.revoluttest.persistence.room.rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateRates(rates: List<RateDTO>)

    @Query("SELECT destinationCurrency, multiplier FROM rate WHERE baseCurrency = :baseCurrency ORDER BY destinationCurrency ASC")
    fun getRatesByAscOrdering(baseCurrency: String): List<RateMinimal>
}
