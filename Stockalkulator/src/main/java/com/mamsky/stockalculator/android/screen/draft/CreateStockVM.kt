package com.mamsky.stockalculator.android.screen.draft

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateStockVM @Inject constructor(

): ViewModel() {

    private val _fieldModel = MutableStateFlow(StockModel("", "", null))
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
            is CreateStockEvent.Description -> {
                _fieldModel.value = _fieldModel.value.copy(description = event.v)
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
            is CreateStockEvent.ROA -> {
                _fieldModel.value = _fieldModel.value.copy(roa = event.v)
            }
            is CreateStockEvent.ROE -> {
                _fieldModel.value = _fieldModel.value.copy(roe = event.v)
            }
            is CreateStockEvent.DER -> {
                _fieldModel.value = _fieldModel.value.copy(der = event.v)
            }
            is CreateStockEvent.Save -> {
                save()
            }
            is CreateStockEvent.Clear -> {
                _fieldModel.value = StockModel("", "", null)
            }
        }
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun calculate() {
        val price = _fieldModel.value.currentPrice?.toDouble() ?: return
        price.setPbv()
        price.per()
    }

    private fun Double.per() {
        val eps = _fieldModel.value.eps?.toDoubleOrNull() ?: return
        _fieldModel.update { it.copy(per = (this / eps).toString()) }
    }

    private fun Double.setPbv() {
        val bvps = _fieldModel.value.bookValue?.toDoubleOrNull() ?: return
        val pbv = this / bvps
        _fieldModel.update { it.copy(pbv = pbv.toString()) }
    }

}

sealed class CreateStockEvent {
    data class Code(val v: String) : CreateStockEvent()
    data class CompanyName(val v: String) : CreateStockEvent()
    data class Description(val v: String) : CreateStockEvent()
    data class Sector(val v: String) : CreateStockEvent()
    data class SubSector(val v: String) : CreateStockEvent()
    data class CurrentPrice(val v: String) : CreateStockEvent()
    data class EPS(val v: String) : CreateStockEvent()
    data class BookValue(val v: String) : CreateStockEvent()
    data class PER(val v: String) : CreateStockEvent()
    data class PBV(val v: String) : CreateStockEvent()
    data class ROE(val v: String): CreateStockEvent()
    data class ROA(val v: String): CreateStockEvent()
    data class DER(val v: String): CreateStockEvent()
    object Save : CreateStockEvent()
    object Clear: CreateStockEvent()
}