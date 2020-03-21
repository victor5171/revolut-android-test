package org.victor5171.revoluttest.rateconversion.ui.formatting

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.text.getSpans
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import java.text.DecimalFormat

class MonetaryTextWatcher(
    private val editText: EditText,
    private val decimalFormat: DecimalFormat
) : TextWatcher {

    private val groupingSeparator = decimalFormat.decimalFormatSymbols.groupingSeparator
    private val decimalCurrencySeparator =
        decimalFormat.decimalFormatSymbols.monetaryDecimalSeparator
    private val diffMatchPatch = DiffMatchPatch()

    var valueChanged: ((value: Double) -> Unit)? = null

    private var isHandlingSpans = false
    private lateinit var copiedTextBeforeChanges: CharSequence

    override fun afterTextChanged(newText: Editable?) {
        if (newText == null) {
            return
        }

        if (isHandlingSpans) {
            return
        }

        val spans = newText.getSpans<FormattingSpan>(0)

        isHandlingSpans = true

        spans.forEach {
            when (it) {
                is DeleteFormattingSpan -> newText.delete(it.start, it.end)
                is InsertFormattingSpan -> newText.insert(it.position, it.charSequence)
            }
        }

        spans.forEach(newText::removeSpan)

        isHandlingSpans = false

        valueChanged?.invoke(newText.toString().filter { it != groupingSeparator }.toDouble())
    }

    override fun beforeTextChanged(
        oldText: CharSequence?,
        startIndex: Int,
        lengthToBeChanged: Int,
        lengthToBeAdded: Int
    ) {
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
        if (isHandlingSpans) {
            return
        }

        if (newText == null) {
            editText.setText("0")

            return
        }

        val newTextLength = newText.length

        if (newText.isEmpty() || (newTextLength == 1 && newText[0] == decimalCurrencySeparator)) {
            editText.text.replace(0, 0, "0")
            return
        }

        val newTextInString = newText.toString()
        val parsedNumber = newTextInString.filter { it != groupingSeparator }.toDoubleOrNull()

        //Wrong number input, we have to reverse it
        if (parsedNumber == null) {
            editText.text.setSpan(
                DeleteFormattingSpan(startIndex, startIndex + lengthToBeAdded),
                0,
                0,
                0
            )

            if (lengthToBeChanged > 0) {
                val charSequenceToInsert =
                    copiedTextBeforeChanges.substring(startIndex, startIndex + lengthToBeChanged)

                editText.text.setSpan(
                    InsertFormattingSpan(startIndex, charSequenceToInsert),
                    0,
                    0,
                    0
                )
            }

            return
        }

        val formattedNumber = decimalFormat.format(parsedNumber)

        val differences = diffMatchPatch.diffMain(newTextInString, formattedNumber)

        var index = 0

        differences.forEach {
            when (it.operation) {
                DiffMatchPatch.Operation.DELETE -> {
                    editText.text.setSpan(
                        DeleteFormattingSpan(index, index + it.text.length),
                        0,
                        0,
                        0
                    )
                }
                DiffMatchPatch.Operation.INSERT -> {
                    editText.text.setSpan(InsertFormattingSpan(index, it.text), 0, 0, 0)
                    index += it.text.length
                }
                DiffMatchPatch.Operation.EQUAL -> {
                    index += it.text.length
                }
            }
        }
    }
}