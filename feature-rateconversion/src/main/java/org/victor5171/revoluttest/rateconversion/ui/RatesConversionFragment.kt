package org.victor5171.revoluttest.rateconversion.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_rates_conversion.*
import org.victor5171.revoluttest.rateconversion.R
import org.victor5171.revoluttest.rateconversion.di.FeatureRateConversionSubComponentContainer
import org.victor5171.revoluttest.rateconversion.numberformatter.NumberFormatter
import org.victor5171.revoluttest.rateconversion.ui.keylistener.KeyListenerBuilder
import org.victor5171.revoluttest.rateconversion.viewmodel.RateConversionViewModel

class RatesConversionFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var keyListenerBuilder: KeyListenerBuilder

    @Inject
    lateinit var numberFormatter: NumberFormatter

    private val convertedRateAdapter by lazy {
        ConvertedRateAdapter(
            keyListenerBuilder,
            numberFormatter,
            this::rateOnFocus
        )
    }

    private val viewModel: RateConversionViewModel by lazy {
        ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[RateConversionViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FeatureRateConversionSubComponentContainer)
            .provideFeatureRateConversionSubComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        viewModel.errorLoadingData.observe(viewLifecycleOwner, Observer {
            ctlOfflineWarning.visibility = if (it != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun rateOnFocus(currencyIdentifier: String, value: Double) {
        viewModel.convert(currencyIdentifier, value)
    }
}
