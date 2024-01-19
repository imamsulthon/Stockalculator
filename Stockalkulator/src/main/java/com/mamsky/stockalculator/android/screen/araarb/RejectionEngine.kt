package com.mamsky.stockalculator.android.screen.araarb

import com.mamsky.stockalculator.android.screen.percentOf

interface RejectionEngine {

    fun calculate(initPrice: Int): List<AutoRejection>
    fun MutableList<AutoRejection>.araList(init: Int): List<AutoRejection>
    fun MutableList<AutoRejection>.arbList(init: Int): List<AutoRejection>

}

class RejectionEngineImpl: RejectionEngine {
    override fun calculate(initPrice: Int): List<AutoRejection> {
        val list = mutableListOf(
            AutoRejection().apply { price = initPrice }
        )
        // upper fraction
        list.araList(initPrice)
        list.arbList(initPrice)
        list.sortByDescending { it.index }
        return list
    }

    override fun MutableList<AutoRejection>.araList(init: Int): List<AutoRejection> {
        var fraction = init.fractionAra()
        var currentPrice: Int = init
        for (i in 1..5) {
            val discPrice = (currentPrice * fraction).toInt()
            var price = currentPrice + discPrice
            val evaluation = price % price.fraction()
            log("getAra evaluation $evaluation")
            price -= evaluation
            val diff = price - init
            this.add(
                AutoRejection(
                    i,
                    price.percentOf(currentPrice),
                    price.percentOf(init),
                    price,
                    diff
                )
            )
            currentPrice = price
            fraction = currentPrice.fractionAra()
        }
        return this
    }

    override fun MutableList<AutoRejection>.arbList(init: Int): List<AutoRejection> {
        var fraction = init.fractionArb()
        var currentPrice: Int = init
        for (i in -1 downTo -5) {
            val discPrice = (currentPrice * fraction).toInt()
            var price = currentPrice - discPrice
            // set limit ARB
            if (price < 50) { price = 50 }
            val evaluation = price % price.fraction()
            price -= evaluation
            val diff = price - init
            val totalPercentage = price.percentOf(init)
            val percentage = price.percentOf(currentPrice)
            log("Arb $fraction $currentPrice $discPrice $price $diff $percentage $totalPercentage")
            this.add(
                AutoRejection(
                    i,
                    percentage,
                    totalPercentage,
                    price,
                    diff
                )
            )
            if (price <= 50) {
                break
            }
            currentPrice = price
            fraction = currentPrice.fractionArb()
        }
        return this
    }

    private fun log(m: String) = println("RejectionEngine $m")

}