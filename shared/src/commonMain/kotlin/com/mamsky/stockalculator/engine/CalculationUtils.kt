package com.mamsky.stockalculator.engine

import com.mamsky.stockalculator.domain.sheet

fun Float?.priceSell(lot: Int, fee: Float = .00f): Float {
    val price = (this ?: 0f) * lot.sheet()
    val fees = price * (fee/100)
    val netPrice = price - fees
    return netPrice
}

fun Float?.priceBuy(lot: Int, fee: Float = .00f): Float {
    val price = (this ?: 0f) * lot.sheet()
    val fees = price * (fee/100)
    val netPrice = price + fees
    return netPrice
}

fun Float?.priceFee(lot: Int, rates: Float = .00f): Float {
    val price = (this ?: 0f) * lot.sheet()
    val fee = price * (rates/100)
    return fee
}

fun Int.priceSell(lot: Int, fee: Float = .00f): Float {
    val price = this * lot.sheet()
    val fees = price * (fee/100)
    val netPrice = price - fees
    return netPrice
}

fun Int.priceBuy(lot: Int, fee: Float = .00f): Float {
    val price = this * lot.sheet()
    val fees = price * (fee/100)
    val netPrice = price + fees
    return netPrice
}

fun Int.priceFee(lot: Int, rates: Float = .00f): Float {
    val price = this * lot.sheet()
    val fee = price * (rates/100)
    return fee
}