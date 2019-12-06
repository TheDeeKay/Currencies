package com.thedeekay.currencies.imageloading

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.util.*
import javax.inject.Inject

/**
 * Implementation of [CurrencyFlagLoader] using Glide library.
 */
class GlideCurrencyFlagLoader @Inject constructor() :
    CurrencyFlagLoader {

    override fun load(currency: Currency, imageView: ImageView) {
        Glide.with(imageView)
            .load(constructUrl(currency))
            .dontAnimate()
//            .apply(RequestOptions.circleCropTransform()) TODO: the images just aren't big enough
            .into(imageView)
    }

    private fun constructUrl(currency: Currency): String {
        val countryCode = currency.currencyCode.substring(0, 2)
        return "https://www.countryflags.io/$countryCode/flat/64.png"
    }
}
