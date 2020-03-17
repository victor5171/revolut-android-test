package org.victor5171.revoluttest.rateconversion.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_rates_conversion.*
import org.victor5171.revoluttest.rateconversion.R
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionSubComponentContainer
import org.victor5171.revoluttest.rateconversion.viewmodel.ConvertedRate
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel
import javax.inject.Inject

class RatesConversionFragment : Fragment() {

    private val convertedRateAdapter by lazy { ConvertedRateAdapter(this::rateOnFocus) }

    @Inject
    lateinit var viewModel: RateConversionViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FeatureRateConversionSubComponentContainer)
            .provideFeatureRateConversionSubComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rates_conversion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = convertedRateAdapter
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.rates.observe(viewLifecycleOwner, Observer {
            convertedRateAdapter.submitList(it)
        })
    }

    private fun rateOnFocus(convertedRate: ConvertedRate) {
        viewModel.convert(convertedRate.currencyIdentifier, 1f)
    }
}
