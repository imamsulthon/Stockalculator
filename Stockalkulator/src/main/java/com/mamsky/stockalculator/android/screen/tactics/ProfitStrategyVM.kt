package com.mamsky.stockalculator.android.screen.tactics

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

    private val _buyItem1 = MutableStateFlow<AverageItem?>(null)
    val buyItem1: StateFlow<AverageItem?> = _buyItem1.asStateFlow()
    private val _buyItem2 = MutableStateFlow<AverageItem?>(null)
    val buyItem2: StateFlow<AverageItem?> = _buyItem2.asStateFlow()
    private val _averageResult = MutableStateFlow<AverageItem?>(null)
    val averageResult: StateFlow<AverageItem?> = _averageResult.asStateFlow()

    private val _currentMktValue = MutableStateFlow<AverageItem?>(null)
    val currentMktValue: StateFlow<AverageItem?> = _currentMktValue.asStateFlow()

    private val _suggestSell = MutableStateFlow<AverageItem?>(null)
    val suggestSell: StateFlow<AverageItem?> = _suggestSell.asStateFlow()

    private val _suggestSell2 = MutableStateFlow<AverageItem?>(null)
    val suggestSell2: StateFlow<AverageItem?> = _suggestSell2.asStateFlow()

    fun calculate(buy1: AverageItem, buy2: AverageItem, fee: Float, targetProfit: Int) {
        viewModelScope.launch {
            _buyItem1.value = buy1
            _buyItem2.value = buy2

            val totalLot = buy1.lot + buy2.lot
            val totalInvested = buy1.average.priceBuy(buy1.lot, fee) + buy2.average.priceBuy(buy2.lot, fee)
            val avg = totalInvested/totalLot.sheet()
            _averageResult.value = AverageItem(
                lot = totalLot,
                average = avg,
                value = totalInvested
            )

            val tempValue = buy2.average.priceBuy(totalLot, fee)
            _currentMktValue.value = AverageItem(
                lot = totalLot,
                average = buy2.average,
                value = tempValue
            )

            val suggestAvg = (totalInvested.toInt() + targetProfit)/totalLot.sheet()
            _suggestSell.value = AverageItem(
                lot = totalLot,
                average = suggestAvg.toFloat(),
                value = totalInvested + targetProfit,
                pl = targetProfit.toFloat()
            )

            // round fraction price
            val suggestPrice2 = suggestAvg.modFraction()
            if (suggestPrice2 == suggestAvg) return@launch

            val totalValue2 = suggestPrice2.priceBuy(totalLot, fee)
            _suggestSell2.value = AverageItem(
                lot = totalLot,
                average = suggestPrice2.toFloat(),
                value = totalValue2,
                pl = totalValue2 - totalInvested
            )
        }

    }

    fun clear() {
        _buyItem1.value = null
        _buyItem2.value = null
        _averageResult.value = null
        _currentMktValue.value = null
        _suggestSell.value = null
        _suggestSell2.value = null
    }

}