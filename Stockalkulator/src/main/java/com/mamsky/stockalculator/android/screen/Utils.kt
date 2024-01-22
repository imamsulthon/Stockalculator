package com.mamsky.stockalculator.android.screen

import java.text.NumberFormat
import java.util.Locale

fun Int?.orZero(): Int = this ?: 0
fun Int?.asString(): String = this?.toString() ?: ""

fun String.onlyNumeric(): Int? = if (this.isEmpty()) null else this.replace("[^0-9]".toRegex(), "").toInt()

fun Int.percentOf(from: Int): Float {
    val diff = (this - from).toDouble()
    return ((diff/from) * 100).toFloat()
}

fun Float.percentFormat(): String {
    return "%.2f".format(this)
}
fun Float.rupiah(): String {
    return this.toInt().rupiah()
}

fun Int.rupiah(): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(this).toString().replace("Rp", "")
}