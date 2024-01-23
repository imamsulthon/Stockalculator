package com.mamsky.stockalculator.android.screen.profit

import androidx.lifecycle.ViewModel
import com.mamsky.stockalculator.android.screen.trading.InputModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfitPerTickVM @Inject constructor(): ViewModel() {

    private val _items = MutableStateFlow(ProfitBundle(0))
    val allItems: StateFlow<ProfitBundle> = _items.asStateFlow()

    fun calculate(price: Int, lot: Int, params: InputModel) {
        val b = ProfitEngineImpl().calculate(price, lot, params.feeForBuy, params.feeForSell)
        _items.update { b }
    }

    fun clear() {
        _items.update { ProfitBundle(0) }
    }

}