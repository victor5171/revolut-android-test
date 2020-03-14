package org.victor5171.revoluttest.persistence.room.rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.victor5171.revoluttest.persistence.rate.RateDAO
import org.victor5171.revoluttest.persistence.rate.RateDTO

@Dao
internal abstract class RoomRateDAO : RateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun updateRates(roomRates: List<RoomRateDTO>)

    override suspend fun updateRates(baseCurrency: String, rates: List<RateDTO>) {
        val roomRateDTOs =
            rates.map { RoomRateDTO(baseCurrency, it.destinationCurrency, it.multiplier) }

        updateRates(roomRateDTOs)
    }

    @Query("SELECT destinationCurrency, multiplier FROM rate WHERE baseCurrency = :baseCurrency ORDER BY destinationCurrency ASC")
    protected abstract fun roomGetRatesByAscOrdering(baseCurrency: String): Flow<List<RateMinimal>>

    override fun getRatesByAscOrdering(baseCurrency: String): Flow<List<RateDTO>> {
        return roomGetRatesByAscOrdering(baseCurrency).map { rates ->
            rates.map { RateDTO(it.destinationCurrency, it.multiplier) }
        }
    }
}
