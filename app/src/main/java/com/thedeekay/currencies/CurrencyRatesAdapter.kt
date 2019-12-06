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

/**
 * RecyclerView adapter that displays currencies, their basic info, and converted amounts.
 */
class CurrencyRatesAdapter(

) : ListAdapter<CurrencyUiModel, CurrencyViewHolder>(CurrencyDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val flagImage: ImageView = itemView.findViewById(R.id.currency_flag_image)
    val currencyCode: TextView = itemView.findViewById(R.id.currency_code_label)
    val currencyName: TextView = itemView.findViewById(R.id.currency_name_label)
    val currencyAmount: EditText = itemView.findViewById(R.id.currency_amount_edit)
}

object CurrencyDiffUtilCallback : DiffUtil.ItemCallback<CurrencyUiModel>() {
    override fun areItemsTheSame(oldItem: CurrencyUiModel, newItem: CurrencyUiModel): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun areContentsTheSame(oldItem: CurrencyUiModel, newItem: CurrencyUiModel): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
