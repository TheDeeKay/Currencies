package com.thedeekay.currencies

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thedeekay.currencies.CurrencyUiModel.ConvertedCurrency
import com.thedeekay.currencies.CurrencyUiModel.MainCurrency

/**
 * RecyclerView adapter that displays currencies, their basic info, and converted amounts.
 */
class CurrencyRatesAdapter(
    private val mainCurrencyListener: MainCurrencyListener
) : ListAdapter<CurrencyUiModel, CurrencyViewHolder>(CurrencyDiffUtilCallback) {

    private val mainAmountTextWatcher = SimpleTextWatcher {
        mainCurrencyListener.newMainCurrencyAmountEntered(it)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainCurrency -> TYPE_MAIN_CURRENCY
            is ConvertedCurrency -> TYPE_CONVERTED_CURRENCY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_list_item, parent, false)
        return when (viewType) {
            TYPE_MAIN_CURRENCY -> MainViewHolder(itemView)
            TYPE_CONVERTED_CURRENCY -> ConvertedViewHolder(itemView)
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        // TODO: load flag image
        val uiModel = getItem(position)
        holder.run {
            currencyCode.text = uiModel.currencyCode
            currencyName.text = uiModel.currencyName

            if (uiModel is MainCurrency) mainAmountTextWatcher.isEnabled = false
            if (currencyAmount.text.toString() != uiModel.amount) {
                currencyAmount.setText(uiModel.amount)
            }
            mainAmountTextWatcher.isEnabled = true

            when (holder) {
                is MainViewHolder -> holder.setCurrencyAmountTextWatcher(mainAmountTextWatcher)
                is ConvertedViewHolder -> {
                    currencyAmount.setOnClickListener {
                        mainCurrencyListener.newMainCurrencySelected(
                            uiModel.amount,
                            uiModel.currencyCode
                        )
                    }
                }
            }
        }
    }
}

abstract class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val flagImage: ImageView = itemView.findViewById(R.id.currency_flag_image)
    val currencyCode: TextView = itemView.findViewById(R.id.currency_code_label)
    val currencyName: TextView = itemView.findViewById(R.id.currency_name_label)
    val currencyAmount: EditText = itemView.findViewById(R.id.currency_amount_edit)
}

class MainViewHolder(itemView: View) : CurrencyViewHolder(itemView) {

    private var activeWatcher: TextWatcher? = null

    fun setCurrencyAmountTextWatcher(watcher: TextWatcher) {
        if (activeWatcher != null) {
            currencyAmount.removeTextChangedListener(activeWatcher)
        }
        activeWatcher = watcher
        currencyAmount.addTextChangedListener(watcher)
    }
}

class ConvertedViewHolder(itemView: View) : CurrencyViewHolder(itemView)

object CurrencyDiffUtilCallback : DiffUtil.ItemCallback<CurrencyUiModel>() {
    override fun areItemsTheSame(
        oldItem: CurrencyUiModel,
        newItem: CurrencyUiModel
    ) = oldItem.currencyCode == newItem.currencyCode

    override fun areContentsTheSame(
        oldItem: CurrencyUiModel,
        newItem: CurrencyUiModel
    ) = oldItem == newItem
}

interface MainCurrencyListener {
    fun newMainCurrencyAmountEntered(amount: String)
    fun newMainCurrencySelected(newMainAmount: String, newCurrencyCode: String)
}

private class SimpleTextWatcher(
    private val listener: (String) -> Unit
) : TextWatcher {

    var isEnabled = true

    override fun afterTextChanged(s: Editable?) {
        if (isEnabled) listener(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // no-op
    }

}

private const val TYPE_MAIN_CURRENCY = 0
private const val TYPE_CONVERTED_CURRENCY = 1
