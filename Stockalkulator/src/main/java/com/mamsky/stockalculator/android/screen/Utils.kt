package com.mamsky.stockalculator.android.screen

import com.mamsky.stockalculator.android.screen.araarb.sheet
import java.text.NumberFormat
import java.util.Locale

fun Int?.notZeroNull() = !this.isZeroOrNull()
fun Int?.isZeroOrNull() = if (this == null) true else this <= 0

fun Int?.orZero(): Int = this ?: 0
fun Int?.asString(): String = this?.toString() ?: ""
fun Float?.asString(): String = this?.toString() ?: ""

fun String.onlyInt(): Int? = if (this.isEmpty()) null else this.replace("[^0-9]".toRegex(), "").toInt()
fun String.onlyFloat(): Float? = if (this.isEmpty()) null else this.toFloatOrNull()

fun Int.percentOf(from: Int): Float {
    val diff = (this - from).toDouble()
    return ((diff/from) * 100).toFloat()
}

fun Int.netPrice(lot: Int, fee: Float = 0f): Int = ((this * lot.sheet()) * (1 - fee)).toInt()

fun Float.percentFormat(): String {
    return "%.2f".format(this)
}
fun Float.rupiah(): String {
    return this.toInt().rupiah()
}

fun Int.rupiah(currency: Boolean = false, fraction: Boolean = true): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    var res = numberFormat.format(this).toString()
    res = if (currency) res else res.replace("Rp", "")
    return if (fraction) res else res.replace(",00", "")
}