package com.mamsky.stockalculator.android.screen.draft

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StockListVM @Inject constructor(): ViewModel() {

    private val _items = mutableStateListOf<StockEntity>()
    val allItems: StateFlow<List<StockEntity>> = MutableStateFlow(_items).asStateFlow()

    fun getAll() {
//        val list = mutableListOf<StockModel>()
//        val itmg = StockModel("ITMG", "Indo Tambang Raya Megah", "Energi dan Gas", subSector = "Batu bara",
//            currentPrice = 26325.0f, eps = 4878.69f, bookValue = 24628.45f, per = 5.40f, pbv = 1.07f)
//        list.add(itmg)
//        for (i in 1..10) {
//            val d = StockModel("ABCD", "PT Bukit Asam $i", "Energi dan Gas",
//                "Gas", 0.0f * i, 0.0f * i, 0.0f * i * 100, 0.0f, 0.0f)
//            list.add(d)
//        }
//
//        _items.clear()
//        _items.addAll(list)
    }
}