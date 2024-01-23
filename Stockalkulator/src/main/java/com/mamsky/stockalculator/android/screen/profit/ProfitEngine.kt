package com.mamsky.stockalculator.android.screen.profit

import com.mamsky.stockalculator.android.screen.araarb.fraction
import com.mamsky.stockalculator.android.screen.netPrice
import com.mamsky.stockalculator.android.screen.percentOf

interface ProfitEngine {

    fun calculate(price: Int, lot: Int, feeBuy: Float, feeSell: Float): ProfitBundle
    fun Int.upperPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick>
    fun Int.belowPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick>

}

class ProfitEngineImpl: ProfitEngine {

    override fun calculate(price: Int, lot: Int, feeBuy: Float, feeSell: Float): ProfitBundle {
        val initValue = price.netPrice(lot, feeBuy)
        val upperValues = initValue.upperPrices(price, lot, feeSell)
        val belowValues = initValue.belowPrices(price, lot, feeSell)
        val list = mutableListOf<ProfitInRow>()
        for (i in 0..9) {
            list.add(
                ProfitInRow(belowValues[i], upperValues[i])
            )
        }
        return ProfitBundle(initValue, upperValues, belowValues, list)
    }

    override fun Int.upperPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick> {
        val upperValues = mutableListOf<ProfitPerTick>()
        var currentPrice = price
        var fraction = price.fraction()

        for (i in 1..10) {
            currentPrice += fraction
            val currentValue = currentPrice.netPrice(lot, feeSell)
            val value = currentValue - this
            val profit = currentValue.percentOf(this)
            fraction = currentPrice.fraction()
            upperValues.add(ProfitPerTick(currentPrice, value, profit))
        }

        return upperValues
    }

    override fun Int.belowPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick> {
        var currentPrice = price
        var fraction = price.fraction()
        val belowValues = mutableListOf(
            ProfitPerTick(price, 0, 0f)
        )
        for (i in -1 downTo -10) {
            currentPrice -= fraction
            val currentValue = currentPrice.netPrice(lot, feeSell)
            val value = currentValue - this
            val profit = currentValue.percentOf(this)
            fraction = currentPrice.fraction()
            belowValues.add(ProfitPerTick(currentPrice, value, profit))
        }

        return belowValues
    }

}