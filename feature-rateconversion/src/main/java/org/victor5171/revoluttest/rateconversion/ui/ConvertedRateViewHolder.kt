package org.victor5171.revoluttest.rateconversion.ui

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_rate.view.*
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.numberformatter.NumberFormatter
import org.victor5171.revoluttest.rateconversion.ui.formatting.MonetaryTextWatcher
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate

class ConvertedRateViewHolder(
    private val listItemRateBinding: ListItemRateBinding,
    private val keyListenerBuilder: KeyListenerBuilder,
    private val numberFormatter: NumberFormatter,
    private val onRateChanged: (currencyIdentifier: String, value: Double) -> Unit
) : RecyclerView.ViewHolder(listItemRateBinding.root) {

    private val monetaryTextWatcher =
        MonetaryTextWatcher(
            itemView.txtValue,
            numberFormatter
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
            listItemRateBinding.formattedValue = numberFormatter.formatFromDouble(convertedRate.value)
        }

        listItemRateBinding.executePendingBindings()
    }
}
