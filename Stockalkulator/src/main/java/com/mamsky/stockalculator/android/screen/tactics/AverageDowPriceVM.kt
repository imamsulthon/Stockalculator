package com.mamsky.stockalculator.android.screen.tactics

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.data.BuyItemModel
import com.mamsky.stockalculator.domain.sheet
import com.mamsky.stockalculator.engine.priceBuy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AverageDowPriceVM @Inject constructor(

): ViewModel() {

    private val _items = mutableStateListOf<BuyItemModel>()
    val allItems: StateFlow<List<BuyItemModel>> = MutableStateFlow(_items).asStateFlow()

    private val _initAverage = MutableStateFlow<AverageItem?>(null)
    val initAverage: StateFlow<AverageItem?> = _initAverage.asStateFlow()

    private val _buyingAverage = MutableStateFlow<AverageItem?>(null)
    val buyingAverage: StateFlow<AverageItem?> = _buyingAverage.asStateFlow()

    private val _averageResult = MutableStateFlow<AverageItem?>(null)
    val averageResult: StateFlow<AverageItem?> = _averageResult.asStateFlow()

    fun calculate(
        init: AverageItem, minPrice: Int, maxPrice: Int, foldPrice: Int = 1,
        minLot: Int = 1, maxLot: Int = 10, lotIncrement: Int = 1, fee: Float = .0f
    ) {
        clear()
        initAverage(init)
        var price = maxPrice
        for (lot in minLot..maxLot step lotIncrement) {
            val pricePerSheet = price.priceBuy(lot, fee).toInt()
            val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                total = pricePerSheet
            }
            _items.add(item)
            price -= foldPrice
            if (price == minPrice || price == 0) break
        }
        calculate()
    }

    fun clear() {
        _items.clear()
        _initAverage.value = null
        _buyingAverage.value = null
        _averageResult.value = null
    }

    private fun initAverage(init: AverageItem) {
        _initAverage.update { init }
    }

    private fun calculate() {
        val total = _items.sumOf { it.price * it.lot }
        val lots = _items.sumOf { it.lot }

        val av = (total.toFloat()/lots)
        val value = av.priceBuy(lots)
        _buyingAverage.update {
            AverageItem(lots, av, value)
        }

        // todo

        _initAverage.value?.let {
            val totalValue = it.value + value
            val totalLot = it.lot + lots
            val average = totalValue/totalLot.sheet()
            _averageResult.update {
                AverageItem(totalLot, average, totalValue)
            }
        }

    }
}