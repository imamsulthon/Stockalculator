package com.mamsky.stockalculator.android.screen.average

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.mamsky.stockalculator.android.screen.araarb.sheet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AveragePriceVM @Inject constructor(): ViewModel() {

    private val _items = mutableStateListOf<BuyItemModel>()
    val allItems: StateFlow<List<BuyItemModel>> = MutableStateFlow(_items).asStateFlow()

    private val _average = MutableStateFlow(AverageItem(0, 0f, 0f))
    val average: StateFlow<AverageItem> = _average.asStateFlow()

    fun buy(price: Int, lot: Int) {
        val x = price * lot.sheet()
        val item = BuyItemModel(id = getId(), price = price, lot = lot).apply {
            total = x
        }
        _items.add(item)
        calculate()
    }

    fun remove(item: BuyItemModel) {
        _items.remove(item)
        calculate()
    }

    fun clear() {
        _items.clear()
    }

    private fun calculate() {
        val total = _items.sumOf { it.price * it.lot.sheet() }
        val lots = _items.sumOf { it.lot }

        val av = (total.toFloat()/lots)
        val value = av * lots
        log("total $total lots $lots av $av value $value")
        _average.update {
            AverageItem(lots, av, value)
        }
    }

    private fun getId() = System.currentTimeMillis()

    private fun log(m:String) {
        println("AveragePrice $m")
    }
}