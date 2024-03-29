package com.thedeekay.currencies


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.toLiveData
import androidx.recyclerview.widget.RecyclerView
import com.thedeekay.currencies.imageloading.CurrencyFlagLoader
import javax.inject.Inject

/**
 * Fragment that displays exchange rates and calculates conversion amounts between different
 * currencies.
 */
class RatesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: RatesViewModelFactory

    @Inject
    lateinit var currencyFlagLoader: CurrencyFlagLoader

    private val ratesViewModel by viewModels<RatesViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        (context.applicationContext as CurrenciesApplication)
            .appComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currencyRatesAdapter =
            CurrencyRatesAdapter(ForwardingMainCurrencyListener(), currencyFlagLoader)
        view.findViewById<RecyclerView>(R.id.currencies_recycler_view).adapter =
            currencyRatesAdapter

        ratesViewModel.currencyAmounts.toLiveData().observe(viewLifecycleOwner, Observer {
            currencyRatesAdapter.submitList(it)
        })
    }

    private inner class ForwardingMainCurrencyListener : MainCurrencyListener {
        override fun newMainCurrencyAmountEntered(amount: String) =
            ratesViewModel.setNewMainCurrencyAmount(amount)

        override fun newMainCurrencySelected(
            newMainAmount: String,
            newCurrencyCode: String
        ) = ratesViewModel.setMainCurrency(newMainAmount, newCurrencyCode)
    }
}
