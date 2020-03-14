package org.victor5171.revoluttest.persistence.room.rate

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "rate",
    primaryKeys = [
        "baseCurrency",
        "destinationCurrency"
    ]
)
data class RateDTO(
    @ColumnInfo(name = "baseCurrency")
    val baseCurrency: String,

    @ColumnInfo(name = "destinationCurrency")
    val destinationCurrency: String,

    @ColumnInfo(name = "multiplier")
    val multiplier: Float
)
