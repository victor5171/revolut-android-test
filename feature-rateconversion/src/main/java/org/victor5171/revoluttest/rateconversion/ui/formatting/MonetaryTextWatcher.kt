package org.victor5171.revoluttest.rateconversion.ui.formatting

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import org.victor5171.revoluttest.rateconversion.numberformatter.NumberFormatter

private const val MAXIMUM_INTEGER_DIGITS = 8

class MonetaryTextWatcher(
    private val editText: EditText,
    private val numberFormatter: NumberFormatter
) : TextWatcher {

    private val diffMatchPatch = DiffMatchPatch()

    var valueChanged: ((value: Double) -> Unit)? = null

    private var isProcessing = false
    private lateinit var copiedTextBeforeChanges: CharSequence

    override fun afterTextChanged(newText: Editable?) {
        if (isProcessing) {
            return
        }

        if (newText == null) {
            return
        }

        convertToDouble(newText)?.let {
            valueChanged?.invoke(it)
        }
    }

    override fun beforeTextChanged(
        oldText: CharSequence?,
        startIndex: Int,
        lengthToBeChanged: Int,
        lengthToBeAdded: Int
    ) {
        if (isProcessing) {
            return
        }

        if (oldText != null) {
            copiedTextBeforeChanges = oldText.toString()
        }
    }

    override fun onTextChanged(
        newText: CharSequence?,
        startIndex: Int,
        lengthToBeChanged: Int,
        lengthToBeAdded: Int
    ) {
        if (isProcessing) {
            return
        }

        if (newText == null) {
            editText.setText("0")
            return
        }

        val newTextLength = newText.length

        if (newText.isEmpty()) {
            return
        }

        if (newTextLength == 1 && newText[0] == numberFormatter.decimalSeparator) {
            editText.text.replace(0, 0, "0")
            return
        }

        val parsedNumber = convertToDouble(newText)

        parsedNumber?.let {
            lockProcessing {
                // Checking if the input exceeds the limit of integer digits
                val positionOfDecimalSeparator = newText.indexOf(numberFormatter.decimalSeparator)
                val positionOfEndOfIntegerDigits =
                    if (positionOfDecimalSeparator != -1) positionOfDecimalSeparator else newText.length
                val numberOfGroupSeparators =
                    newText.count { it == numberFormatter.groupingSeparator }
                if ((startIndex + lengthToBeAdded) <= positionOfEndOfIntegerDigits && positionOfEndOfIntegerDigits > MAXIMUM_INTEGER_DIGITS + numberOfGroupSeparators) {
                    editText.text.delete(startIndex, startIndex + lengthToBeAdded)
                }

                // After removing the exceeding input, format the new number, compare the changes, and apply them
                val formattedNumber = numberFormatter.formatFromUnformattedString(editText.text)

                val newTextInString = newText.toString()
                val differences = diffMatchPatch.diffMain(newTextInString, formattedNumber)

                var index = 0
                differences.forEach {
                    when (it.operation) {
                        DiffMatchPatch.Operation.DELETE -> {
                            editText.text.delete(index, index + it.text.length)
                        }
                        DiffMatchPatch.Operation.INSERT -> {
                            editText.text.insert(index, it.text)
                            index += it.text.length
                        }
                        DiffMatchPatch.Operation.EQUAL -> {
                            index += it.text.length
                        }
                        null -> {
                        }
                    }
                }
            }
        } ?: run {
            // Wrong number input, we have to reverse it
            lockProcessing {
                editText.text.delete(startIndex, startIndex + lengthToBeAdded)

                if (lengthToBeChanged > 0) {
                    val charSequenceToInsert =
                        copiedTextBeforeChanges.substring(
                            startIndex,
                            startIndex + lengthToBeChanged
                        )

                    editText.text.insert(startIndex, charSequenceToInsert)
                }
            }
        }
    }

    private fun lockProcessing(function: () -> Unit) {
        isProcessing = true
        function()
        isProcessing = false
    }

    private fun convertToDouble(text: CharSequence) = numberFormatter.tryParse(text.toString())
}
