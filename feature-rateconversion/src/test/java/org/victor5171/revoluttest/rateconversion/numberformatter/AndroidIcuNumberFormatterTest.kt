package org.victor5171.revoluttest.rateconversion.numberformatter

import android.os.Build
import java.util.Locale
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AndroidIcuNumberFormatterTest {

    @Test
    fun `When I try to format a integer number, with 0 on decimal value, it shouldn't give a value with the decimal separator`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        val formattedValue = androidIcuNumberFormatter.formatFromDouble(1.00000)
        Assert.assertEquals("1", formattedValue)
    }

    @Test
    fun `When I try to format a double value, with more than 2 digits on decimal value, it should trim to 2 digits`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        val formattedValue = androidIcuNumberFormatter.formatFromDouble(1.111)
        Assert.assertEquals("1.11", formattedValue)
    }

    @Test
    fun `When I try to parse an invalid number string, it should return null`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals(null, androidIcuNumberFormatter.tryParse("..1.0"))
    }

    @Test
    fun `When I try to parse a valid number string, with decimal separators, it should work`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals(
            1234.12,
            androidIcuNumberFormatter.tryParse(
                "1${androidIcuNumberFormatter.groupingSeparator}234${androidIcuNumberFormatter.decimalSeparator}12"
            )
        )
    }

    @Test
    fun `When I try to format an unformatted string from a integer only number, it should give no fraction digits`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals("123,123", androidIcuNumberFormatter.formatFromUnformattedString("123123"))
    }

    @Test
    fun `When I try to format an unformatted string from a integer only number, but with decimal separator, it should give the decimal separator back`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals("123,123.", androidIcuNumberFormatter.formatFromUnformattedString("123123."))
    }

    @Test
    fun `When I try to format an unformatted string with decimal separator and 0, it should give the decimal separator and 0 back`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals("123,123.0", androidIcuNumberFormatter.formatFromUnformattedString("123123.0"))
    }

    @Test
    fun `When I try to format an unformatted string with decimal separator but more than 2 fraction digits, it should trim to 2`() {
        val androidIcuNumberFormatter = AndroidIcuNumberFormatter(Locale.US)
        Assert.assertEquals("123,123.12", androidIcuNumberFormatter.formatFromUnformattedString("123123.123"))
    }
}
