package org.victor5171.revoluttest.rateconversion.ui

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_rate.view.*
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.ui.formatting.MonetaryTextWatcher
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate

class ConvertedRateViewHolder(
    private val listItemRateBinding: ListItemRateBinding,
    private val keyListenerBuilder: KeyListenerBuilder,
    numberFormat: NumberFormat,
    private val decimalFormat: DecimalFormat,
    private val onRateChanged: (currencyIdentifier: String, value: Double) -> Unit
) : RecyclerView.ViewHolder(listItemRateBinding.root) {

    private val monetaryTextWatcher =
        MonetaryTextWatcher(
            itemView.txtValue,
            numberFormat,
            decimalFormat
        )

    init {
        with(itemView.txtValue) {
            keyListener = keyListenerBuilder.build()
        }
    }

    fun bind(convertedRate: ConvertedRate) {
        itemView.setOnClickListener {
            itemView.txtValue.requestFocus()
        }

        val txtValue = itemView.txtValue

        with(txtValue) {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    addTextChangedListener(monetaryTextWatcher)
                    monetaryTextWatcher.valueChanged = { value ->
                        onRateChanged(convertedRate.currencyIdentifier, value)
                    }

                    onRateChanged(convertedRate.currencyIdentifier, convertedRate.value)

                    return@setOnFocusChangeListener
                }

                removeTextChangedListener(monetaryTextWatcher)
                monetaryTextWatcher.valueChanged = null
            }
        }

        listItemRateBinding.icon = CurrencyIconRetriever.getIcon(
            listItemRateBinding.root.context,
            convertedRate.currencyIdentifier
        )
        listItemRateBinding.currencyIdentifier = convertedRate.currencyIdentifier
        listItemRateBinding.currencyName = convertedRate.currencyName

        if (!txtValue.isFocused) {
            listItemRateBinding.formattedValue = decimalFormat.format(convertedRate.value)
        }

        listItemRateBinding.executePendingBindings()
    }
}
