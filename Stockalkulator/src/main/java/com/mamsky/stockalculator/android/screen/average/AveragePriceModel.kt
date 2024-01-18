package com.mamsky.stockalculator.android.screen.average

data class BuyItemModel(
    val id: Long,
    var price: Int,
    var lot: Int,
) {
    var total: Int = 0
}

data class AverageItem(
    var lot: Int,
    var average: Float,
    var value: Float,
)