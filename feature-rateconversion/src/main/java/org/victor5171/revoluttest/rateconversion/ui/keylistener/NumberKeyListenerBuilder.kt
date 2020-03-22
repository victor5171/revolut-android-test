package org.victor5171.revoluttest.rateconversion.ui.keylistener

import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import javax.inject.Inject
import org.victor5171.revoluttest.rateconversion.di.DecimalSeparator

class NumberKeyListenerBuilder @Inject constructor(@DecimalSeparator decimalSeparator: Char?) : KeyListenerBuilder {

    private val keyListener: KeyListener

    init {
        val zeroToNine = (0..9).joinToString(separator = "") { it.toString() }
        val digits = "$zeroToNine$decimalSeparator,"
        keyListener = DigitsKeyListener.getInstance(digits)
    }

    override fun build(): KeyListener {
        return keyListener
    }
}
