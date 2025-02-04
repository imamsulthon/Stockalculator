package com.mamsky.stockalculator.android.screen.trading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamsky.stockalculator.data.InputModel
import com.mamsky.stockalculator.data.ResultBuy
import com.mamsky.stockalculator.data.ResultCalculation
import com.mamsky.stockalculator.data.ResultSell
import com.mamsky.stockalculator.engine.priceBuy
import com.mamsky.stockalculator.engine.priceFee
import com.mamsky.stockalculator.engine.priceSell
import com.mamsky.stockalculator.utils.percentOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradingReturnVM @Inject constructor(): ViewModel() {

    private val _buyData = MutableStateFlow(ResultBuy())
    val buyData: StateFlow<ResultBuy> = _buyData.asStateFlow()

    private val _sellData = MutableStateFlow(ResultSell())
    val sellData: StateFlow<ResultSell> = _sellData.asStateFlow()

    private val _result = MutableStateFlow(ResultCalculation())
    val result: StateFlow<ResultCalculation> = _result.asStateFlow()

    fun initCalculate(params: InputModel) {
        val buyValue =  params.buy.priceBuy(params.lot)
        val buyFee = params.buy.priceFee(params.lot, params.feeForBuy)
        val buyNet =  params.buy.priceBuy(params.lot, params.feeForBuy)
        _buyData.update {
            it.copy(
                price = params.buy.toFloat(),
                lot = params.lot,
                buyValue = buyValue,
                fee = params.feeForBuy,
                buyFee = buyFee,
                totalPaid = buyNet
            )
        }
        val sellValue = params.sell.priceSell(params.lot)
        val sellFee = params.sell.priceFee(params.lot, params.feeForSell)
        val sellNet = params.sell.priceSell(params.lot, params.feeForSell)
        _sellData.update {
            it.copy(
                sellPrice = params.sell.toFloat(),
                lot = params.lot,
                sellValue = sellValue,
                fee = params.feeForSell,
                sellFee = sellFee,
                totalReceived = sellNet
            )
        }
        val profit = sellValue - buyValue
        val totalFee = buyFee + sellFee
        val netProfit = profit - totalFee
        val percentPL = sellNet.toInt().percentOf(buyNet.toInt())
        _result.update {
            it.copy(
                profit = profit,
                netProfit = netProfit,
                totalFee = totalFee,
                status = if (profit < 0f) "Loss" else "Profit",
                percentPL = percentPL
            )
        }
    }

    fun clear() {
        viewModelScope.launch {
            _buyData.update { ResultBuy() }
            _sellData.update { ResultSell() }
            _result.update { ResultCalculation() }
        }
    }

}