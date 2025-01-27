package com.mamsky.stockalculator.data

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
    var pl: Float = 0f
)