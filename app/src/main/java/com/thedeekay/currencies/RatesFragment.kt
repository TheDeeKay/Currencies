package com.thedeekay.currencies


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.toLiveData
import androidx.recyclerview.widget.RecyclerView

/**
 * Fragment that displays exchange rates and calculates conversion amounts between different
 * currencies.
 */
class RatesFragment : Fragment() {

    private val ratesViewModel by viewModels<RatesViewModel> {
        (requireContext().applicationContext as CurrenciesApplication).appComponent.ratesViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currencyRatesAdapter = CurrencyRatesAdapter()
        ratesViewModel.currencyAmounts.toLiveData().observe(viewLifecycleOwner, Observer {
            currencyRatesAdapter.submitList(it)
        })
        view.findViewById<RecyclerView>(R.id.currencies_recycler_view).adapter =
            currencyRatesAdapter
    }
}
