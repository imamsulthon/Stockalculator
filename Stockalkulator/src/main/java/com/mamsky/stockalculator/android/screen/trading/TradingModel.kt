package com.mamsky.stockalculator.android.screen.trading


data class InputModel(
    var buy: Int,
    var sell: Int,
    var lot: Int,
    var feeForBuy: Float,
    var feeForSell: Float,
) {
    constructor() : this(0, 0, 0, 0f, 0f)
}

data class ResultCalculation(
    val status: String,
    val profit: Float,
    val totalFee: Float,
    val netProfit: Float
) {
    constructor(): this("", 0f, 0f, 0f)
}

data class ResultSell(
    val sellPrice: Float,
    val lot: Int,
    val sellValue: Float,
    val fee: Float,
    val sellFee: Float,
    val totalReceived: Float
) {
    constructor(): this(0f, 0,0f, 0f, 0f,0f)

}

data class ResultBuy(
    val price: Float,
    val lot: Int,
    val buyValue: Float,
    val fee: Float,
    val buyFee: Float,
    val totalPaid: Float
) {
    constructor(): this(0f,0, 0f, 0f, 0f,0f)

}