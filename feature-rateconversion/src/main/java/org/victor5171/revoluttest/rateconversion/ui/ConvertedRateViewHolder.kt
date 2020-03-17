package org.victor5171.revoluttest.rateconversion.ui

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_rate.view.*
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate

class ConvertedRateViewHolder(
    private val listItemRateBinding: ListItemRateBinding,
    private val onFocus: (convertedRate: ConvertedRate) -> Unit
) : RecyclerView.ViewHolder(listItemRateBinding.root) {

    private var currentConvertedRate: ConvertedRate? = null

    fun bind(convertedRate: ConvertedRate) {
        itemView.setOnClickListener {
            onFocus(convertedRate)
        }

        itemView.txtValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onFocus(convertedRate)
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

        if (currentConvertedRate?.value != convertedRate.value) {
            listItemRateBinding.value = convertedRate.value
        }

        currentConvertedRate = convertedRate

        listItemRateBinding.executePendingBindings()
    }
}