package com.mamsky.stockalculator.android.screen.draft

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateStockVM @Inject constructor(): ViewModel() {

    private val _fieldModel = MutableStateFlow(StockModel("", "", "", "",
        0.0f, 0.0f,0.0f,0.0f, 0.0f))
    val fieldModel = _fieldModel.asStateFlow()

    fun events(event: CreateStockEvent) {
        when (event) {
            is CreateStockEvent.Code -> {
                _fieldModel.value = _fieldModel.value.copy(code = event.v)
            }
            is CreateStockEvent.CompanyName -> {
                _fieldModel.value = _fieldModel.value.copy(companyName = event.v)
            }
            is CreateStockEvent.Sector -> {
                _fieldModel.value = _fieldModel.value.copy(sector = event.v)
            }
            is CreateStockEvent.SubSector -> {
                _fieldModel.value = _fieldModel.value.copy(subSector = event.v)
            }
            is CreateStockEvent.CurrentPrice -> {
                _fieldModel.value = _fieldModel.value.copy(currentPrice = event.v)
                calculate()
            }
            is CreateStockEvent.EPS -> {
                _fieldModel.value = _fieldModel.value.copy(eps = event.v)
                calculate()
            }
            is CreateStockEvent.BookValue -> {
                _fieldModel.value = _fieldModel.value.copy(bookValue = event.v)
                calculate()
            }
            is CreateStockEvent.PER -> {
                _fieldModel.value = _fieldModel.value.copy(per = event.v)
            }
            is CreateStockEvent.PBV -> {
                _fieldModel.value = _fieldModel.value.copy(pbv = event.v)
            }
            is CreateStockEvent.Save -> {}
            is CreateStockEvent.Clear -> {}
        }
    }

    private fun calculate() {
        val price = _fieldModel.value.currentPrice
        price.setPbv()
        price.per()
    }

    private fun Float.per() {
        _fieldModel.update { it.copy(per = this * it.eps) }
    }

    private fun Float.setPbv() {
        _fieldModel.update { it.copy(pbv = this * it.bookValue) }
    }

}

sealed class CreateStockEvent {
    data class Code(val v: String) : CreateStockEvent()
    data class CompanyName(val v: String) : CreateStockEvent()
    data class Sector(val v: String) : CreateStockEvent()
    data class SubSector(val v: String) : CreateStockEvent()
    data class CurrentPrice(val v: Float) : CreateStockEvent()
    data class EPS(val v: Float) : CreateStockEvent()
    data class BookValue(val v: Float) : CreateStockEvent()
    data class PER(val v: Float) : CreateStockEvent()
    data class PBV(val v: Float) : CreateStockEvent()
    object Save : CreateStockEvent()
    object Clear: CreateStockEvent()
}