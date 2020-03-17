package org.victor5171.revoluttest.rateconversion.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.victor5171.revoluttest.rateconversion.databinding.ListItemRateBinding
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate

class ConvertedRateAdapter(
    private val onFocus: (convertedRate: ConvertedRate) -> Unit
) : ListAdapter<ConvertedRate, ConvertedRateViewHolder>(DiffUtil) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedRateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItemRateBinding = ListItemRateBinding.inflate(layoutInflater, parent, false)
        return ConvertedRateViewHolder(listItemRateBinding, onFocus)
    }

    override fun onBindViewHolder(holder: ConvertedRateViewHolder, position: Int) {
        val convertedRate = getItem(position)
        holder.bind(convertedRate)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).currencyIdentifier.hashCode().toLong()
    }
}

private object DiffUtil : DiffUtil.ItemCallback<ConvertedRate>() {
    override fun areItemsTheSame(oldItem: ConvertedRate, newItem: ConvertedRate): Boolean {
        return oldItem.currencyIdentifier == newItem.currencyIdentifier
    }

    override fun areContentsTheSame(oldItem: ConvertedRate, newItem: ConvertedRate): Boolean {
        return oldItem == newItem
    }
}