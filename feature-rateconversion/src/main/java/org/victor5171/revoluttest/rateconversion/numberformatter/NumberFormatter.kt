package org.victor5171.revoluttest.rateconversion.numberformatter

/**
 * Interface used on this feature to handle number formatting hard-work
 */
interface NumberFormatter {

    val decimalSeparator: Char

    val groupingSeparator: Char

    /**
     * Should format a double value to string
     */
    fun formatFromDouble(doubleValue: Double): String

    /**
     * Should apply grouping rules and respect the decimal separator existence
     */
    fun formatFromUnformattedString(unformattedCharSequence: CharSequence): String

    /**
     * Try to format a string to double
     * @return The parsed value, or null in case it's not a invalid double Value
     */
    fun tryParse(stringValue: String): Double?
}
