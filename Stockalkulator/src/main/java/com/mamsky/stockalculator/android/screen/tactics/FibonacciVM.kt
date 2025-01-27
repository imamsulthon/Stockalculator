package com.mamsky.stockalculator.android.screen.tactics

import com.mamsky.stockalculator.android.screen.tactics.LotSequence.reverted
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.data.BuyItemModel
import com.mamsky.stockalculator.engine.priceBuy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FibonacciVM @Inject constructor(): AverageDowPriceVM() {


    private val _avgModel = MutableStateFlow(AvgDownModel())
    val avgModel = _avgModel.asStateFlow()

    fun fibonacci(
        init: AverageItem, startPrice: Int, endPrice: Int, foldPrice: Int = 1,
        lotIncrement: Int = 1, fee: Float = .0f,
        revertLot: Boolean = false, uptrend: Boolean = false
    ) {
        clear()
        initAverage(init, fee)

        when {
            uptrend && revertLot -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) { false }.reverted {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price += foldPrice
                    price >= endPrice
                }
            }
            uptrend && !revertLot -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price += foldPrice
                    price >= endPrice
                }
            }
            revertLot -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) { false }.reverted {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    _allItems.add(item)
                    price -= foldPrice
                    price <= endPrice
                }
            }
            else -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    price -= foldPrice
                    _allItems.add(item)
                    price <= endPrice
                }
            }
        }

        calculate()
    }

    fun events(event: AvgFormEvent) {
        when (event) {
            is AvgFormEvent.startPrice -> _avgModel.value = _avgModel.value.copy(startPrice = event.v)
            is AvgFormEvent.endPrice -> _avgModel.value = _avgModel.value.copy(endPrice = event.v)
            is AvgFormEvent.foldPrice -> _avgModel.value = _avgModel.value.copy(foldPrice = event.v)
            is AvgFormEvent.startLot -> _avgModel.value = _avgModel.value.copy(startLot = event.v)
            is AvgFormEvent.factorLot -> _avgModel.value = _avgModel.value.copy(factorLot = event.v)
            is AvgFormEvent.fee -> _avgModel.value = _avgModel.value.copy(fee = event.v)
            is AvgFormEvent.revertLot -> _avgModel.value = _avgModel.value.copy(revertLot = event.v)
            else -> {}
        }
    }

}