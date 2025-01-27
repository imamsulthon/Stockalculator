package com.mamsky.stockalculator.android.screen.draft

data class StockModel(
    val code: String,
    val companyName: String,
    val sector: String,
    val subSector: String,
    val eps: Float,
    val bookValue: Float,
    val currentPrice: Float,
    val per: Float?,
    val pbv: Float?,
)

