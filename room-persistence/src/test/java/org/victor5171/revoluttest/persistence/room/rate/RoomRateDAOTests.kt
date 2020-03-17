package org.victor5171.revoluttest.persistence.room.rate

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.victor5171.revoluttest.persistence.rate.RateDTO
import org.victor5171.revoluttest.persistence.room.AppDatabase

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RoomRateDAOTests {

    private lateinit var appDatabase: AppDatabase

    private lateinit var roomRateDAO: RoomRateDAO

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        roomRateDAO = appDatabase.rateDao()
    }

    @After
    fun after() {
        appDatabase.close()
    }

    @Test
    fun `When I try to update rates, it should work`() = runBlocking {
        roomRateDAO.updateRates(
            "EUR",
            listOf(
                RateDTO("USD", 1.14f),
                RateDTO("BRL", 5f)
            )
        )
    }

    @Test
    fun `When I try to get the rates at first, it should give an empty list`() = runBlocking {
        val rates = roomRateDAO.getRatesByAscOrdering("EUR").first()
        Assert.assertTrue(rates.isEmpty())
    }

    @Test
    fun `When I try to add the rates, and then get them, it should work`() = runBlocking {
        roomRateDAO.updateRates(
            "EUR",
            listOf(
                RateDTO("BRL", 5f),
                RateDTO("USD", 1.14f)
            )
        )

        val rates = roomRateDAO.getRatesByAscOrdering("EUR").first()

        Assert.assertEquals(RateDTO("BRL", 5f), rates[0])
        Assert.assertEquals(RateDTO("USD", 1.14f), rates[1])
    }

    @Test
    fun `When I add rates, and then change them, they should have the new values`() = runBlocking {
        roomRateDAO.updateRates(
            "EUR",
            listOf(
                RateDTO("BRL", 5f),
                RateDTO("USD", 1.14f)
            )
        )

        roomRateDAO.updateRates(
            "EUR",
            listOf(
                RateDTO("BRL", 5.20f),
                RateDTO("USD", 1.20f)
            )
        )

        val rates = roomRateDAO.getRatesByAscOrdering("EUR").first()

        Assert.assertEquals(RateDTO("BRL", 5.20f), rates[0])
        Assert.assertEquals(RateDTO("USD", 1.20f), rates[1])
    }
}
