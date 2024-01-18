package com.mamsky.stockalculator.android.screen

import java.text.NumberFormat
import java.util.Locale


fun String.onlyNumeric(): Int = if (this.isEmpty()) 0 else this.replace("[^0-9]".toRegex(), "").toInt()

fun String.asRupiah() = rupiah(this.onlyNumeric().toInt())

fun Int.asRupiah() = rupiah(this)

fun rupiah(number: Int): String {
    val localeID =  Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(number).toString()
}