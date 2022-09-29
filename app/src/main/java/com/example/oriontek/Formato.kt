package com.example.oriontek
/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Formato {

    fun No(value: Long) : String{
        val df = DecimalFormat("000000")
        df.decimalFormatSymbols = DecimalFormatSymbols(Locale.US)

        return  df.format(value)  //000001
    }
}