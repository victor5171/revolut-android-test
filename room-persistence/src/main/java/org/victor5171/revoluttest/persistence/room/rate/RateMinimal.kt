package org.victor5171.revoluttest.persistence.room.rate

import androidx.room.ColumnInfo

internal data class RateMinimal(
    @ColumnInfo(name = "destinationCurrency")
    val destinationCurrency: String,

    @ColumnInfo(name = "multiplier")
    val multiplier: Double
)
