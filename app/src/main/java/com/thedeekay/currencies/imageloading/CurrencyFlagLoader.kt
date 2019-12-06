package com.thedeekay.currencies.imageloading

import android.widget.ImageView
import java.util.*

/**
 * Simple interface for loading currency flags into ImageViews.
 */
interface CurrencyFlagLoader {

    /**
     * Trigger asynchronous loading of flag image into the given ImageView.
     */
    fun load(currency: Currency, imageView: ImageView)

}
