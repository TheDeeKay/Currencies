package com.thedeekay.currencies

import com.thedeekay.domain.Money
import java.math.BigDecimal
import java.util.*
import kotlin.math.max

sealed class CurrencyUiModel {

    abstract val currencyCode: String
    abstract val currencyName: String
    abstract val amount: String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    data class MainCurrency(
        override val currencyCode: String,
        override val currencyName: String,
        override val amount: String
    ) : CurrencyUiModel() {

        constructor(currency: Currency, amount: String) : this(
            currency.currencyCode,
            currency.displayName,
            amount
        )

    }

    data class ConvertedCurrency(
        override val currencyCode: String,
        override val currencyName: String,
        override val amount: String
    ) : CurrencyUiModel() {

        constructor(money: Money) : this(
            money.currency.currencyCode,
            money.currency.displayName,
            formatAmount(money)
        )

        companion object {
            private fun formatAmount(money: Money): String {
                if (money.amount.compareTo(BigDecimal.ZERO) == 0) return ""
                return String.format("%.${money.decimalPlaces()}f", money.amount.toDouble())
            }
        }

    }
}

private fun Money.decimalPlaces(): Int = max(currency.defaultFractionDigits, 0)
