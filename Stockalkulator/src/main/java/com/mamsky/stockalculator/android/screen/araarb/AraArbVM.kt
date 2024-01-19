package com.mamsky.stockalculator.android.screen.araarb

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AraArbVM @Inject constructor(): ViewModel() {

    private val _items = mutableStateListOf<AutoRejection>()
    val allItems: StateFlow<List<AutoRejection>> = MutableStateFlow(_items).asStateFlow()

    fun calculate(initPrice: Int) {
        val result = RejectionEngineImpl().calculate(initPrice)
        _items.clear()
        _items.addAll(result)
    }

}