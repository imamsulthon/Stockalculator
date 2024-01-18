package com.mamsky.stockalculator.android.screen.trading

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        val buyValue = params.buy.toFloat() * params.lot
        val buyFee = buyValue * params.feeForBuy
        _buyData.update {
            it.copy(
                price = params.buy.toFloat(),
                lot = params.lot,
                buyValue = buyValue,
                fee = params.feeForBuy,
                buyFee = buyFee,
                totalPaid = buyValue - buyFee
            )
        }
        val sellValue = params.sell.toFloat() * params.lot
        val sellFee = params.feeForSell * sellValue
        _sellData.update {
            it.copy(
                sellPrice = params.sell.toFloat(),
                lot = params.lot,
                sellValue = sellValue,
                fee = params.feeForSell,
                sellFee = sellFee,
                totalReceived = sellValue - sellFee
            )
        }
        val profit: Double = ((params.sell - params.buy) * params.lot).toDouble()
        val netProfit = (sellValue - sellFee) - (buyValue - buyFee)
        val totalFee = buyFee + sellFee
        _result.update {
            it.copy(
                profit = profit.toFloat(),
                netProfit = netProfit,
                totalFee = totalFee,
                status = if (profit < 0f) "Loss" else "Profit",
            )
        }
    }

}