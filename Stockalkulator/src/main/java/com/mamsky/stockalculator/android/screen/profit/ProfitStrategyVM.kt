package com.mamsky.stockalculator.android.screen.profit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.domain.modFraction
import com.mamsky.stockalculator.domain.sheet
import com.mamsky.stockalculator.engine.priceBuy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfitStrategyVM @Inject constructor(): ViewModel() {

    private val _averageResult = MutableStateFlow<AverageItem?>(null)
    val averageResult: StateFlow<AverageItem?> = _averageResult.asStateFlow()

    private val _currentMktValue = MutableStateFlow<AverageItem?>(null)
    val currentMktValue: StateFlow<AverageItem?> = _currentMktValue.asStateFlow()

    private val _suggestSell = MutableStateFlow<AverageItem?>(null)
    val suggestSell: StateFlow<AverageItem?> = _suggestSell.asStateFlow()

    private val _suggestSell2 = MutableStateFlow<AverageItem?>(null)
    val suggestSell2: StateFlow<AverageItem?> = _suggestSell2.asStateFlow()

    fun calculate(
        buy1: AverageItem,
        buy2: AverageItem,
        buyFee: Float,
        targetProfit: Int,
        targetProfit2: Float? = null,
    ) {
        viewModelScope.launch {

            val totalLot = buy1.lot + buy2.lot
            val totalInvested = buy1.average.priceBuy(buy1.lot, buyFee) + buy2.average.priceBuy(buy2.lot, buyFee)
            val avg = totalInvested/totalLot.sheet()
            _averageResult.value = AverageItem(
                lot = totalLot,
                average = avg,
                value = totalInvested
            )

            val tempValue = buy2.average.priceBuy(totalLot, buyFee)
            _currentMktValue.value = AverageItem(
                lot = totalLot,
                average = buy2.average,
                value = tempValue
            )

            var suggestAvg: Int = 0
            if (targetProfit2 == null) {
                suggestAvg = (totalInvested.toInt() + targetProfit)/totalLot.sheet()
                _suggestSell.value = AverageItem(
                    lot = totalLot,
                    average = suggestAvg.toFloat(),
                    value = totalInvested + targetProfit,
                    pl = targetProfit.toFloat()
                )
            } else {
                val target = totalInvested + (totalInvested * (targetProfit2/100))
                suggestAvg = (target/totalLot.sheet()).toInt()
                _suggestSell.value = AverageItem(
                    lot = totalLot,
                    average = suggestAvg.toFloat(),
                    value = target,
                    pl = target - totalInvested
                )
            }

            // round fraction price, suggestion for target profit 1
            val suggestPrice2 = suggestAvg.modFraction()
            if (suggestPrice2 > 0) {
                val totalValue2 = suggestPrice2.priceBuy(totalLot, buyFee)
                _suggestSell2.value = AverageItem(
                    lot = totalLot,
                    average = suggestPrice2.toFloat(),
                    value = totalValue2,
                    pl = totalValue2 - totalInvested
                )
            }

        }

    }

    fun clear() {
        _averageResult.value = null
        _currentMktValue.value = null
        _suggestSell.value = null
        _suggestSell2.value = null
    }

}