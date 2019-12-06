package com.thedeekay.currencies

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

/**
 * RecyclerView adapter that displays currencies, their basic info, and converted amounts.
 */
class CurrencyRatesAdapter :
    ListAdapter<CurrencyUiModel, CurrencyViewHolder>(CurrencyDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        // TODO: load flag image
        val uiModel = getItem(position)
        holder.run {
            currencyCode.text = uiModel.currencyCode
            currencyName.text = uiModel.currencyName
            if (uiModel is ConvertedCurrency) currencyAmount.setText(uiModel.amount)
        }

        // TODO: add different listeners
    }
}

class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val flagImage: ImageView = itemView.findViewById(R.id.currency_flag_image)
    val currencyCode: TextView = itemView.findViewById(R.id.currency_code_label)
    val currencyName: TextView = itemView.findViewById(R.id.currency_name_label)
    val currencyAmount: EditText = itemView.findViewById(R.id.currency_amount_edit)
}

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
