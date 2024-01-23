package com.mamsky.stockalculator.android.screen.profit


data class ProfitBundle(
    val initialValue: Int,
    val upperValues: List<ProfitPerTick> = listOf(),
    val belowValues: List<ProfitPerTick> = listOf(),
    val list: List<ProfitInRow> = listOf()
)

data class ProfitInRow(
    val left: ProfitPerTick,
    val right: ProfitPerTick? = null
)

data class ProfitPerTick(
    val price: Int,
    val value: Int,
    val lossGain: Float,
)