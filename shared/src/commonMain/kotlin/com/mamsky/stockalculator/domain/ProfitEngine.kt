package com.mamsky.stockalculator.domain

import com.mamsky.stockalculator.data.ProfitBundle
import com.mamsky.stockalculator.data.ProfitInRow
import com.mamsky.stockalculator.data.ProfitPerTick
import com.mamsky.stockalculator.utils.netPrice
import com.mamsky.stockalculator.utils.percentOf

interface ProfitEngine {

    fun calculate(price: Int, lot: Int, feeBuy: Float, feeSell: Float): ProfitBundle
    fun Int.upperPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick>
    fun Int.belowPrices(price: Int, lot: Int, feeSell: Float): MutableList<ProfitPerTick>

}

class ProfitEngineImpl: ProfitEngine {

    override fun calculate(price: Int, lot: Int, feeBuy: Float, feeSell: Float): ProfitBundle {
        val list = mutableListOf<ProfitInRow>()
        val initValue = price.netPrice(lot, feeBuy)
        val fraction = price.fraction()
        val mod = price % fraction
        val dPrice = if (mod > 0) price.downFold0() else price
        val uPrice = if (mod > 0) price.downFold0().upFold0() else price
        val belowValues = initValue.belowPrices(dPrice, lot, feeSell)
        val upperValues = initValue.upperPrices(uPrice, lot, feeSell)
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

    private fun Int.upFold0(): Int {
        val fr = this.fraction()
        val temp = (this % fr)
        return if (temp > 0) this + fr - temp else this
    }

    private fun Int.downFold0(): Int {
        val fr = this.fraction()
        val temp = (this % fr)
        return if (temp > 0) this - temp else this
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