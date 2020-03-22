package org.victor5171.revoluttest.rateconversion.ui.keylistener

import android.icu.text.DecimalFormat
import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import javax.inject.Inject

class NumberKeyListenerBuilder @Inject constructor(decimalFormat: DecimalFormat) :
    KeyListenerBuilder {

    private val keyListener: KeyListener

    init {
        val zeroToNine = (0..9).joinToString(separator = "") { it.toString() }
        val decimalFormatSymbols = decimalFormat.decimalFormatSymbols
        with(decimalFormatSymbols) {
            val digits =
                "$zeroToNine$decimalSeparator${if (decimalFormat.isGroupingUsed) groupingSeparator else ""}"
            keyListener = DigitsKeyListener.getInstance(digits)
        }
    }

    override fun build(): KeyListener {
        return keyListener
    }
}
