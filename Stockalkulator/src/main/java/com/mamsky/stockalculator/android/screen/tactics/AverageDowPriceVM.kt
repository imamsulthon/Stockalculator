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
open class AverageDowPriceVM @Inject constructor(

): ViewModel() {

    internal val _allItems = mutableStateListOf<BuyItemModel>()
    val allItems: StateFlow<List<BuyItemModel>> = MutableStateFlow(_allItems).asStateFlow()

    private val _initAverage = MutableStateFlow<AverageItem?>(null)
    val initAverage: StateFlow<AverageItem?> = _initAverage.asStateFlow()

    private val _buyingAverage = MutableStateFlow<AverageItem?>(null)
    val buyingAverage: StateFlow<AverageItem?> = _buyingAverage.asStateFlow()

    private val _averageResult = MutableStateFlow<AverageItem?>(null)
    val averageResult: StateFlow<AverageItem?> = _averageResult.asStateFlow()


    fun exercise(
        init: AverageItem, startPrice: Int, endPrice: Int, foldPrice: Int = 1,
        minLot: Int = 1, maxLot: Int = 10, lotIncrement: Int = 1, fee: Float = .0f,
        revertLot: Boolean = false, uptrend: Boolean = false
    ) {
        clear()
        initAverage(init, fee)
        when {
            uptrend && !revertLot -> {
                var price = startPrice
                LotSequence.antiGeneral(maxLot, minLot, lotIncrement) { lot ->
                    println("LotSequence1 $lot")
                    val pricePerSheet = price.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price += foldPrice
                    price >= endPrice
                }
            }
            uptrend && revertLot -> {
                println("LotSequence2 $lotIncrement")
                var price = startPrice
                LotSequence.general(minLot, maxLot, lotIncrement) { lot ->
                    val pricePerSheet = price.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price += foldPrice
                    price >= endPrice
                }
            }
            !uptrend && revertLot -> {
                println("LotSequence3")
                var price = startPrice
                LotSequence.antiGeneral(maxLot, minLot, lotIncrement) { lot ->
                    val pricePerSheet = price.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price -= foldPrice
                    price <= endPrice
                }
            }
            else -> {
                println("LotSequence4 $lotIncrement")
                var price = startPrice
                LotSequence.general(minLot, maxLot, lotIncrement) { lot ->
                    val pricePerSheet = price.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price -= foldPrice
                    price <= endPrice
                }
            }
        }
        calculate()
    }

    fun clear() {
        _allItems.clear()
        _initAverage.value = null
        _buyingAverage.value = null
        _averageResult.value = null
    }

    open fun initAverage(init: AverageItem, fee: Float) {
        val price = init.average.priceBuy(init.lot, fee)
        _initAverage.update {
            init.copy(value = price)
        }
    }

    open fun calculate() {
        val total = _allItems.sumOf { it.price * it.lot }
        val lots = _allItems.sumOf { it.lot }

        val av = (total.toFloat()/lots)
        val value = av.priceBuy(lots)
        _buyingAverage.update {
            AverageItem(lots, av, value)
        }

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