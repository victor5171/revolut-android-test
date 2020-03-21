package org.victor5171.revoluttest.rateconversion.ui.formatting

sealed class FormattingSpan
class DeleteFormattingSpan(val start: Int, val end: Int) : FormattingSpan()
data class InsertFormattingSpan(val position: Int, val charSequence: CharSequence) : FormattingSpan()