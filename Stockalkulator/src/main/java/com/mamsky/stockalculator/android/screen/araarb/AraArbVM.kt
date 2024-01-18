package com.mamsky.stockalculator.android.screen.araarb

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs


@HiltViewModel
class AraArbVM @Inject constructor(): ViewModel() {

    private val _items = mutableStateListOf<AutoRejection>()
    val allItems: StateFlow<List<AutoRejection>> = MutableStateFlow(_items).asStateFlow()

    fun calculate(initPrice: Int) {
        val list = mutableListOf(
            AutoRejection().apply { price = initPrice }
        )
        // upper fraction
        list.getAra(initPrice)
        list.arb(initPrice)
        list.sortByDescending { it.increase }
        _items.clear()
        _items.addAll(list)
    }

    private fun MutableList<AutoRejection>.getAra(init: Int): List<AutoRejection> {
        var fraction = init.fractionAra()
        var currentPrice = init
        for (i in 1..5) {
            var factor = 1 + fraction
            currentPrice = (factor * init).toInt()
            val x = currentPrice % currentPrice.fraction()
            log("onFraction $x $currentPrice")
            val increase = currentPrice - init
            this.add(
                AutoRejection(i, ((fraction - 1) * 100), (fraction * 100), currentPrice, increase)
            )
            fraction += 0.25f
        }
        return this
    }

    private fun log(m: String) = Timber.d(m)

    private fun MutableList<AutoRejection>.arb(init: Int): List<AutoRejection> {
        var fraction = init.fractionArb()
        var currentPrice = init
        for (i in -1 downTo -5) {
            val factor = -1 - fraction
            currentPrice = abs((factor * init).toInt())
            val increase = currentPrice - init
            if (currentPrice <= 0) break
            this.add(
                AutoRejection(i, ((1 - fraction) * 100), (fraction * 100), currentPrice, increase)
            )
            fraction -= 0.25f
        }
        return this
    }


}