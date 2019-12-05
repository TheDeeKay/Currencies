package com.thedeekay.exchangerates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import java.util.*

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM exchangeRate WHERE base = :base")
    fun allExchangeRates(base:Currency): Flowable<List<ExchangeRateEntity>>

    @Insert
    fun insertExchangeRates(exchangeRates: List<ExchangeRateEntity>)

}
