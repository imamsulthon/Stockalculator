package com.mamsky.stockalculator.android.screen.tactics

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.mamsky.stockalculator.android.screen.tactics.LotSequence.reverted
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
                    _items.add(item)
                    price += foldPrice
                    false
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
                    _items.add(item)
                    price += foldPrice
                    false
                }
            }
            !uptrend && revertLot -> {
                println("LotSequence3")
                revertLot(startPrice, endPrice, foldPrice, minLot, maxLot, lotIncrement, fee)
            }
            else -> {
                println("LotSequence4 $lotIncrement")
                var price = startPrice
                LotSequence.general(minLot, maxLot, lotIncrement) { lot ->
                    val pricePerSheet = price.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _items.add(item)
                    price -= foldPrice
                    false
                }
            }
        }
        calculate()
    }

    private fun revertLot(
        topPrice: Int, bottomPrice: Int, foldPrice: Int = 1,
        minLot: Int = 1, maxLot: Int = 10, lotIncrement: Int = 1, fee: Float = .0f,
    ) {
        var price = topPrice
        LotSequence.antiGeneral(maxLot, minLot, lotIncrement) { lot ->
            println("LotSequence2 $lot")
            val pricePerSheet = price.priceBuy(lot, fee).toInt()
            val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                total = pricePerSheet
            }
            _items.add(item)
            price -= foldPrice
//            price < bottomPrice || price == 0
            false
        }
        calculate()
    }

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
                    _items.add(item)
                    price += foldPrice
//                    price >= endPrice
                    false
                }
            }
            uptrend && !revertLot -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    _items.add(item)
                    price += foldPrice
//                    price >= endPrice
                    false
                }
            }
            revertLot -> {
                var price = startPrice
                LotSequence.fibonacci(lotIncrement) { false }.reverted {
                    val pricePerSheet = price.priceBuy(it, fee).toInt()
                    val item = BuyItemModel(id = it.toLong(), price = price, lot = it).apply {
                        total = pricePerSheet
                    }
                    _items.add(item)
                    price -= foldPrice
//                    price >= endPrice
                    false
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
                    _items.add(item)
//                    price >= endPrice
                    false
                }
            }
        }

        calculate()
    }

    fun martingale(
        init: AverageItem, startPrice: Int, endPrice: Int, foldPrice: Int = 1, startLot: Int,
        recursion: Int = 1, fee: Float = .0f,
        revertLot: Boolean = false, uptrend: Boolean = false
    ) {
        clear()
        initAverage(init, fee)
        when {
            uptrend && !revertLot -> {
                var price = startPrice
                LotSequence.martingale(startLot, recursion) { lot ->
                    val pricePerSheet = startPrice.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    price += foldPrice
                    _items.add(item)
                    price >= endPrice
                }
            }
            uptrend && revertLot -> {
                var price = startPrice
                LotSequence.martingale(startLot, recursion) { _ -> false }.reverted { lot ->
                    val pricePerSheet = startPrice.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _items.add(item)
                    price += foldPrice
                    price >= endPrice
                }
            }
            revertLot -> {
                var price = startPrice
                LotSequence.martingale(startLot, recursion) { _ -> false }.reverted { lot ->
                    val pricePerSheet = startPrice.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    _items.add(item)
                    price -= foldPrice
                    price <= endPrice
                }
            }
            else -> {
                var price = startPrice
                LotSequence.martingale(startLot, recursion) { lot ->
                    val pricePerSheet = startPrice.priceBuy(lot, fee).toInt()
                    val item = BuyItemModel(id = lot.toLong(), price = price, lot = lot).apply {
                        total = pricePerSheet
                    }
                    price -= foldPrice
                    _items.add(item)
                    price <= endPrice
                }
            }
        }
        calculate()
    }

    fun clear() {
        _items.clear()
        _initAverage.value = null
        _buyingAverage.value = null
        _averageResult.value = null
    }

    private fun initAverage(init: AverageItem, fee: Float) {
        val price = init.average.priceBuy(init.lot, fee)
        _initAverage.update {
            init.copy(value = price)
        }
    }

    private fun calculate() {
        val total = _items.sumOf { it.price * it.lot }
        val lots = _items.sumOf { it.lot }

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