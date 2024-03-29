package com.thedeekay.exchangerates.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

@Dao
internal abstract class ExchangeRatesDao {

    @Query("SELECT * FROM exchangeRate WHERE base = :base")
    protected abstract fun allExchangeRateEntities(base: Currency): Flowable<List<ExchangeRateEntity>>

    fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> =
        allExchangeRateEntities(base).map { rates -> rates.map { it.toDomain() } }

    @Insert
    protected abstract fun insertExchangeRateEntities(exchangeRates: List<ExchangeRateEntity>)

    @Query("DELETE FROM exchangeRate WHERE base = :base")
    protected abstract fun clearDataForBase(base: Currency)

    @Transaction
    open fun insertExchangeRates(exchangeRates: List<ExchangeRate>, base: Currency) {
        clearDataForBase(base)

        insertExchangeRateEntities(exchangeRates.map {
            ExchangeRateEntity(
                it
            )
        })
    }

}
