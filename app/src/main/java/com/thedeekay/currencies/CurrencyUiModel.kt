package com.thedeekay.currencies

sealed class CurrencyUiModel {

    abstract val currencyCode: String
    abstract val currencyName: String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    data class MainCurrency(
        override val currencyCode: String,
        override val currencyName: String
    ) : CurrencyUiModel()

    data class ConvertedCurrency(
        override val currencyCode: String,
        override val currencyName: String,
        val amount: String
    ) : CurrencyUiModel()
}