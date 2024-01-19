package com.mamsky.stockalculator.android.screen

import java.text.NumberFormat
import java.util.Locale


fun String.onlyNumeric(): Int = if (this.isEmpty()) 0 else this.replace("[^0-9]".toRegex(), "").toInt()

fun String.asRupiah() = rupiah(this.onlyNumeric())

fun Int.asRupiah() = rupiah(this)

fun Int.percentOf(from: Int): Float {
    val diff = (this - from).toDouble()
    return ((diff/from) * 100).toFloat()
}

fun Float.percentFormat(): String {
    return "%.2f".format(this)
}

fun rupiah(number: Int): String {
    val localeID =  Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(number).toString()
}