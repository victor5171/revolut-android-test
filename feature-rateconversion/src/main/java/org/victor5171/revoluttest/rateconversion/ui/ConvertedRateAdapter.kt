package org.victor5171.revoluttest.rateconversion.ui

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.AdapterItem
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate
import org.victor5171.revoluttest.rateconversion.viewmodel.LoadingItem

private const val VIEW_TYPE_FOR_CURRENCY = 0
private const val VIEW_TYPE_FOR_LOADING = 1

class ConvertedRateAdapter(
    private val keyListenerBuilder: KeyListenerBuilder,
    private val numberFormat: NumberFormat,
    private val decimalFormat: DecimalFormat,
    private val onRateChanged: (currencyIdentifier: String, value: Double) -> Unit
) : ListAdapter<AdapterItem, RecyclerView.ViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FOR_CURRENCY -> {
                val listItemRateBinding = ListItemRateBinding.inflate(layoutInflater, parent, false)
                return ConvertedRateViewHolder(
                    listItemRateBinding,
                    keyListenerBuilder,
                    numberFormat,
                    decimalFormat,
                    onRateChanged
                )
            }
            VIEW_TYPE_FOR_LOADING -> LoadingViewHolder(layoutInflater, parent)
            else -> throw UnsupportedOperationException("Unknown viewType: $viewType!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ConvertedRate -> VIEW_TYPE_FOR_CURRENCY
            LoadingItem -> VIEW_TYPE_FOR_LOADING
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ConvertedRateViewHolder) {
            val convertedRate = getItem(position)
            holder.bind(convertedRate as ConvertedRate)
        }
    }
}

private object DiffUtil : DiffUtil.ItemCallback<AdapterItem>() {
    override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
        if (oldItem is ConvertedRate && newItem is ConvertedRate) {
            return oldItem.currencyIdentifier == newItem.currencyIdentifier
        }

        if (oldItem is LoadingItem && newItem is LoadingItem) {
            return true
        }

        return false
    }

    override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
        return oldItem == newItem
    }
}
