package org.victor5171.revoluttest.rateconversion.ui

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_rate.view.*
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate

class ConvertedRateViewHolder(
    private val listItemRateBinding: ListItemRateBinding,
    private val onRateChanged: (currencyIdentifier: String, value: Float) -> Unit
) : RecyclerView.ViewHolder(listItemRateBinding.root) {

    private var currentConvertedRate: ConvertedRate? = null

    fun bind(convertedRate: ConvertedRate) {
        itemView.setOnClickListener {
            itemView.txtValue.requestFocus()
        }

        val txtValue = itemView.txtValue

        with(txtValue) {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    onRateChanged(convertedRate.currencyIdentifier, convertedRate.value)
                }
            }

            doAfterTextChanged {
                if (isFocused) {
                    onRateChanged(convertedRate.currencyIdentifier, it.toString().toFloat())
                }
            }
        }

        if (currentConvertedRate?.currencyIdentifier != convertedRate.currencyIdentifier) {
            listItemRateBinding.icon = CurrencyIconRetriever.getIcon(
                listItemRateBinding.root.context,
                convertedRate.currencyIdentifier
            )

            listItemRateBinding.currencyIdentifier = convertedRate.currencyIdentifier
        }

        if (currentConvertedRate?.currencyName != convertedRate.currencyName) {
            listItemRateBinding.currencyName = convertedRate.currencyName
        }

        if (currentConvertedRate?.value != convertedRate.value && !txtValue.isFocused) {
            listItemRateBinding.value = convertedRate.value
        }

        currentConvertedRate = convertedRate

        listItemRateBinding.executePendingBindings()
    }
}