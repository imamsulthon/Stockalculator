package com.mamsky.stockalculator.utils

import com.mamsky.stockalculator.domain.fraction
import com.mamsky.stockalculator.domain.sheet
import java.text.NumberFormat
import java.util.Locale

fun Int?.notZeroNull() = !this.isZeroOrNull()
fun Int?.isZeroOrNull() = if (this == null) true else this <= 0

fun Int?.orZero(): Int = this ?: 0
fun Int?.asString(): String = this?.toString() ?: ""
fun Float?.asString(): String = this?.toString() ?: ""
fun Double?.asString(): String = this?.toString() ?: ""

fun Int?.upFold(f: Int = 1, limit: Int? = null): Int {
    if (this == null) return 0
    val test = this + f
    if (limit != null && this > limit) return this
    return test
}

fun Int?.upFold0(): Int {
    val fr = this?.fraction() ?: 1
    val temp = (this?:0) % fr
    return if (temp > 0) this.upFold(fr - temp) else this.upFold(this?.fraction() ?: 1)
}

fun Int?.downFold(f: Int = 1, limit: Int? = 0): Int {
    if (this == null) return this ?: 0
    val test = this - f
    if (limit != null && this <= limit) return this
    return test
}

fun Int?.downFold0(): Int {
    val fr = this?.fraction() ?: 1
    val temp = (this?:0) % fr
    if (temp > 0) return this.downFold(temp)
    return this.downFold(fr)
}

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

fun Any.percent(): String = "$this%"

fun Float.rupiah(): String {
    return this.toInt().rupiah(currency = true, fraction = false)
}

fun Float.rupiah(fraction: Boolean): String {
    return this.toInt().rupiah(currency = true, fraction = fraction)
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