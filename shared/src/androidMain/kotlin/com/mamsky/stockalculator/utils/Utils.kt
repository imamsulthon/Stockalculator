package com.mamsky.stockalculator.utils

import com.mamsky.stockalculator.domain.sheet
import java.text.NumberFormat
import java.util.Locale

fun Int?.notZeroNull() = !this.isZeroOrNull()
fun Int?.isZeroOrNull() = if (this == null) true else this <= 0

fun Int?.orZero(): Int = this ?: 0
fun Int?.asString(): String = this?.toString() ?: ""
fun Float?.asString(): String = this?.toString() ?: ""

private val floatRegex = "[0-9]+(\\.[0-9]+)?\$"
fun String.onlyInt(): Int? = if (this.isEmpty()) null else this.replace("[^0-9]".toRegex(), "").toInt()
fun String.onlyFloat(): Float? = if (this.isEmpty()) null else this.toFloatOrNull()
fun String?.intOrNull(): Int? = if (this.isNullOrEmpty() || this.isBlank()) null else this.replace("[^0-9]".toRegex(), "").toInt()

fun Int.percentOf(from: Int): Float {
    val diff = (this - from).toDouble()
    return ((diff/from) * 100).toFloat()
}

fun Int.netPrice(lot: Int, fee: Float = 0f): Int = ((this * lot.sheet()) * (1 - fee)).toInt()

fun Float.percentFormat(): String {
    return "%.2f".format(this)
}

fun Float.rupiah(): String {
    return this.toInt().rupiah(currency = true, fraction = false)
}

fun Float.rupiah(fraction: Boolean): String {
    return this.toInt().rupiah(currency = true)
}

fun Int?.rupiah(currency: Boolean = false, fraction: Boolean = true): String {
    return this?.rupiah(currency, fraction) ?: "-"
}

fun Int.rupiah(currency: Boolean = false, fraction: Boolean = true): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    var res = numberFormat.format(this).toString()
    res = if (currency) res else res.replace("Rp", "")
    return if (fraction) res else res.replace(",00", "")
}

fun String.currency(label: String = "Rp"): String = "$label$this"