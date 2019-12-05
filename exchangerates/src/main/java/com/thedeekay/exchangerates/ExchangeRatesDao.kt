package com.thedeekay.exchangerates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.thedeekay.domain.ExchangeRate
import io.reactivex.Flowable
import java.util.*

@Dao
abstract class ExchangeRatesDao {

    @Query("SELECT * FROM exchangeRate WHERE base = :base")
    protected abstract fun allExchangeRateEntities(base: Currency): Flowable<List<ExchangeRateEntity>>

    fun allExchangeRates(base: Currency): Flowable<List<ExchangeRate>> =
        allExchangeRateEntities(base).map { rates -> rates.map { it.toDomain() } }

    @Insert
    protected abstract fun insertExchangeRateEntities(exchangeRates: List<ExchangeRateEntity>)

    fun insertExchangeRates(exchangeRates: List<ExchangeRate>) {
        insertExchangeRateEntities(exchangeRates.map { ExchangeRateEntity(it) })
    }

}
