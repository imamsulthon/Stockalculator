package com.mamsky.stockalculator.android.screen.araarb

data class AutoRejection(
    val index: Int,
    val percentage: Float,
    val totalPercentage: Float,
    var price: Int,
    val increase: Int,
) {

    constructor(): this(0, 0f, 0f, 0, 0)

    val type: Int = if (increase > 0) ARType.ARA.index else if (increase == 0) ARType.EQUAL.index else ARType.ARB.index

}

enum class ARType(val index: Int) {
    ARA(1), EQUAL(0), ARB(-1)
}

