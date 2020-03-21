package org.victor5171.revoluttest.rateconversion.viewmodel

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.victor5171.revoluttest.rateconversion.R
import org.victor5171.revoluttest.rateconversion.ui.CurrencyIconRetriever

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ConvertedRateTest {
    @Test
    fun `When I try to get a icon for a currency that doesn't have a drawable, it should return a default one`() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val drawable = CurrencyIconRetriever.getIcon(context, "TES")

        Assert.assertEquals(
            context.getDrawable(R.drawable.ic_missing_flag)!!.constantState,
            drawable.constantState
        )
    }

    @Test
    fun `When I try to get a icon for a currency that have a drawable, it should return that drawable`() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val drawable = CurrencyIconRetriever.getIcon(context, "EUR")

        Assert.assertEquals(
            context.getDrawable(R.drawable.ic_eur)!!.constantState,
            drawable.constantState
        )
    }
}