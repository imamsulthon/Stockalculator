package com.mamsky.stockalculator.android.screen.tactics

data class AvgDownModel(
    var topPrice: Int,
    var bottomPrice: Int,
    var factorPrice: Int,
    var topLot: Int,
    var bottomLot: Int,
    var factorLot: Int,
    var fee: Float = 0f,
)
