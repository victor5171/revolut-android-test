package org.victor5171.revoluttest.rateconversion.ui

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_rate.view.*
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.ui.formatting.MonetaryTextWatcher
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate
import java.text.DecimalFormat

class ConvertedRateViewHolder(
    private val listItemRateBinding: ListItemRateBinding,
    private val keyListenerBuilder: KeyListenerBuilder,
    private val decimalFormat: DecimalFormat,
    private val onRateChanged: (currencyIdentifier: String, value: Double) -> Unit
) : RecyclerView.ViewHolder(listItemRateBinding.root) {

    private val monetaryTextWatcher =
        MonetaryTextWatcher(
            itemView.txtValue,
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
            removeTextChangedListener(monetaryTextWatcher)
            monetaryTextWatcher.valueChanged = null

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    onRateChanged(convertedRate.currencyIdentifier, convertedRate.value)
                }
            }

            if (adapterPosition == 0) {
                addTextChangedListener(monetaryTextWatcher)
                monetaryTextWatcher.valueChanged = { value ->
                    onRateChanged(convertedRate.currencyIdentifier, value)
                }
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