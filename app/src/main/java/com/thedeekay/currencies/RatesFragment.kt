package com.thedeekay.currencies


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency
import com.thedeekay.domain.GBP
import com.thedeekay.domain.JPY
import com.thedeekay.domain.USD

/**
 * Fragment that displays exchange rates and calculates conversion amounts between different
 * currencies.
 */
class RatesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currencyRatesAdapter = CurrencyRatesAdapter()
        currencyRatesAdapter.submitList(
            listOf(
                MainCurrency(USD, "USD", "US Dollar"),
                ConvertedCurrency(GBP, "GBP", "British pound", "123.45"),
                ConvertedCurrency(JPY, "JPY", "Japanese Yen", "1234.56")
            )
        )
        view.findViewById<RecyclerView>(R.id.currencies_recycler_view)
            .adapter = currencyRatesAdapter
    }
}
