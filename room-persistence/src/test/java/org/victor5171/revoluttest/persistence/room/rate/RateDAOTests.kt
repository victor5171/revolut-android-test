package org.victor5171.revoluttest.persistence.room.rate

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.victor5171.revoluttest.persistence.room.AppDatabase

@RunWith(RobolectricTestRunner::class)
class RateDAOTests {

    private lateinit var appDatabase: AppDatabase

    private lateinit var rateDAO: RateDAO

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        rateDAO = appDatabase.rateDao()
    }

    @After
    fun after() {
        appDatabase.close()
    }

    @Test
    fun `When I try to update rates, it should work`() {
        rateDAO.updateRates(listOf(
            RateDTO("EUR", "USD", 1.14f),
            RateDTO("EUR", "BRL", 5f)
        ))
    }

    @Test
    fun `When I try to get the rates at first, it should give an empty list`() {
        val rates = rateDAO.getRatesByAscOrdering("EUR")
        Assert.assertTrue(rates.isEmpty())
    }

    @Test
    fun `When I try to add the rates, and then get them, it should work`() {
        rateDAO.updateRates(listOf(
            RateDTO("EUR", "BRL", 5f),
            RateDTO("EUR", "USD", 1.14f)
        ))

        val rates = rateDAO.getRatesByAscOrdering("EUR")

        Assert.assertEquals(RateMinimal("BRL", 5f), rates[0])
        Assert.assertEquals(RateMinimal("USD", 1.14f), rates[1])
    }

    @Test
    fun `When I add rates, and then change them, they should have the new values`() {
        rateDAO.updateRates(listOf(
            RateDTO("EUR", "BRL", 5f),
            RateDTO("EUR", "USD", 1.14f)
        ))

        rateDAO.updateRates(listOf(
            RateDTO("EUR", "BRL", 5.20f),
            RateDTO("EUR", "USD", 1.20f)
        ))

        val rates = rateDAO.getRatesByAscOrdering("EUR")

        Assert.assertEquals(RateMinimal("BRL", 5.20f), rates[0])
        Assert.assertEquals(RateMinimal("USD", 1.20f), rates[1])
    }
}
