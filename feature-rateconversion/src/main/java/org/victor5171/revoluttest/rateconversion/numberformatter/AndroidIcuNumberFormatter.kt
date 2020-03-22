package org.victor5171.revoluttest.rateconversion.numberformatter

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.min

private const val MAXIMUM_NUMBER_OF_FRACTION_DIGITS = 2

class AndroidIcuNumberFormatter @Inject constructor(
    locale: Locale
) : NumberFormatter {

    private val numberFormatWithFraction = NumberFormat.getNumberInstance(locale)
    private val numberFormatWithoutFraction = NumberFormat.getNumberInstance(locale)

    private val decimalFormat = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat

    init {
        numberFormatWithFraction.maximumFractionDigits = MAXIMUM_NUMBER_OF_FRACTION_DIGITS

        numberFormatWithoutFraction.maximumFractionDigits = 0

        decimalFormat.decimalFormatSymbols.apply {
            currencySymbol = ""
        }
    }

    override val decimalSeparator = decimalFormat.decimalFormatSymbols.monetaryDecimalSeparator

    override val groupingSeparator = decimalFormat.decimalFormatSymbols.groupingSeparator

    override fun formatFromDouble(doubleValue: Double): String {
        return numberFormatWithFraction.format(doubleValue)
    }

    override fun formatFromUnformattedString(unformattedCharSequence: CharSequence): String {
        val unformattedString = unformattedCharSequence.toString()
        val parsedValue = numberFormatWithoutFraction.parse(unformattedString)

        val integerPart = numberFormatWithoutFraction.format(parsedValue)

        val indexOfDecimalSeparator = unformattedString.indexOf(decimalSeparator)
        if (indexOfDecimalSeparator != -1) {
            val fractionSubstringAndWithoutSeparators =
                unformattedString.substring(indexOfDecimalSeparator + 1, unformattedString.length)
                    .replace(decimalSeparator.toString(), "")
            val endIndex = min(fractionSubstringAndWithoutSeparators.length, MAXIMUM_NUMBER_OF_FRACTION_DIGITS)
            val reducedFractionSubstring =
                fractionSubstringAndWithoutSeparators.substring(0, endIndex)
            return integerPart + decimalSeparator + reducedFractionSubstring
        }

        return integerPart
    }

    override fun tryParse(stringValue: String): Double? {
        return stringValue.runCatching { numberFormatWithFraction.parse(stringValue).toDouble() }
            .getOrNull()
    }
}
