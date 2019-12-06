package com.thedeekay.currencies

import java.util.*

sealed class CurrencyUiModel {

    abstract val currency: Currency
    abstract val currencyCode: String
    abstract val currencyName: String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    data class MainCurrency(
        override val currency: Currency,
        override val currencyCode: String,
        override val currencyName: String
    ) : CurrencyUiModel()

    data class ConvertedCurrency(
        override val currency: Currency,
        override val currencyCode: String,
        override val currencyName: String,
        val amount: String
    ) : CurrencyUiModel()
}
