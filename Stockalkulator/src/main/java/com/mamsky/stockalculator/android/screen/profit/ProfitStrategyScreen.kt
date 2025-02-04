package com.mamsky.stockalculator.android.screen.profit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.fee.ChangeFeeModal
import com.mamsky.stockalculator.android.screen.fee.UseBrokerFee
import com.mamsky.stockalculator.android.screen.fee.rememberBuyFeeDefault
import com.mamsky.stockalculator.android.screen.fee.rememberSellFeeDefault
import com.mamsky.stockalculator.android.screen.fee.setWith
import com.mamsky.stockalculator.android.screen.tactics.RowItemRes
import com.mamsky.stockalculator.android.shared.ButtonAndClear
import com.mamsky.stockalculator.android.shared.CustomTab
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputField3
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.android.shared.rememberCurrencyVisualTransformation
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.engine.priceBuy
import com.mamsky.stockalculator.utils.asString
import com.mamsky.stockalculator.utils.downFold
import com.mamsky.stockalculator.utils.downFold0
import com.mamsky.stockalculator.utils.onlyInt
import com.mamsky.stockalculator.utils.orZero
import com.mamsky.stockalculator.utils.rupiah
import com.mamsky.stockalculator.utils.upFold
import com.mamsky.stockalculator.utils.upFold0

@Composable
fun ProfitStrategyScreen(
    navController: NavController,
    viewModel: ProfitStrategyVM = hiltViewModel()
) {

    val result by viewModel.averageResult.collectAsState()
    val currentMarketValue by viewModel.currentMktValue.collectAsState()
    val suggestSell by viewModel.suggestSell.collectAsState()
    val suggestSell2 by viewModel.suggestSell2.collectAsState()

    Content(
        navController,
        avgResult = result,
        currentValue = currentMarketValue,
        suggestSell = suggestSell,
        suggestSell2 = suggestSell2,
        onClear = viewModel::clear,
        onCalculate = { b1, b2, f, s1,  s2 ->
            viewModel.calculate(b1, b2, f, s1, s2)
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    navController: NavController,
    avgResult: AverageItem?,
    currentValue: AverageItem?,
    suggestSell: AverageItem? = null,
    suggestSell2: AverageItem? = null,
    onClear: () -> Unit,
    onCalculate: (AverageItem, AverageItem, Float, Int, Float?) -> Unit,
) {

    val transformation = rememberCurrencyVisualTransformation()

    val targetProfitType2 = listOf("Rp", "%")
    val (selectedTarget, setSelectedTarget) = remember { mutableIntStateOf(0) }

    var initAveragePrice: Int? by remember { mutableStateOf(null) }
    var initLot: Int? by remember { mutableStateOf(null) }
    val initInvested: Int? by remember(initLot, initAveragePrice) {
        mutableIntStateOf(initAveragePrice.orZero().priceBuy(initLot ?: 0).toInt())
    }

    var price1: Int? by remember { mutableStateOf(null) }
    var lot1: Int? by remember { mutableStateOf(null) }
    val additionInvested: Int? by remember(price1, lot1) {
        mutableIntStateOf(price1.orZero().priceBuy(lot1 ?: 0).toInt())
    }

    var targetProfit1: Int? by remember { mutableStateOf(null) }
    var targetPercentage: String? by remember { mutableStateOf(null) }

    var useBrokerFee by remember { mutableStateOf(false) }
    var buyFee by rememberBuyFeeDefault()
    var sellFee by rememberSellFeeDefault()
    var showFeeDialog by remember { mutableStateOf(false) }

    val enableButton by remember(initAveragePrice, initLot, price1, lot1) {
        mutableStateOf(initAveragePrice != null && initLot != null && price1 != null && lot1 != null)
    }

    MainContent("Profit Strategy", onBack = navController::popBackStack) {
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            item {
                Text(text = "Initial Investment", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    InputField(
                        modifier = Modifier.weight(1f).padding(end = 5.dp),
                        label = "Average Price",
                        value = initAveragePrice.asString(),
                        onValueChange = {
                            initAveragePrice = it.onlyInt()
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        imeAction = ImeAction.Next,
                        visualTransformation = transformation
                    )
                    InputField3(
                        modifier = Modifier.weight(1f).padding(start = 5.dp),
                        label = "Lot",
                        usePrefix = false,
                        useUpDown = true,
                        up = {
                            initLot = initLot.upFold()
                        },
                        down = {
                            initLot = initLot.downFold()
                        },
                        value = initLot.asString(),
                        onValueChange = {
                            initLot = it.onlyInt()
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        imeAction = ImeAction.Next
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Est. Invested:", style = MaterialTheme.typography.bodySmall)
                    HSpacer(5.dp)
                    Text(initInvested.orZero().rupiah(true), style = MaterialTheme.typography.bodySmall)
                }
                VSpacer(10.dp)
            }

            item {
                Text(text = "Buying on Current Price", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    InputField3(
                        modifier = Modifier.weight(1f).padding(end = 5.dp),
                        label = "Price",
                        value = price1.asString(),
                        onValueChange = {
                            price1 = it.onlyInt()
                        },
                        useUpDown = true,
                        usePrefix = false,
                        up = { price1 = price1.upFold0() },
                        down = { price1 = price1.downFold0() },
                        visualTransformation = rememberCurrencyVisualTransformation(),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        imeAction = ImeAction.Next,
                    )
                    InputField3(
                        modifier = Modifier.weight(1f).padding(start = 5.dp),
                        label = "Lot",
                        usePrefix = false,
                        useUpDown = true,
                        up = {
                            lot1 = lot1.upFold()
                        },
                        down = {
                            lot1 = lot1.downFold()
                        },
                        value = lot1.asString(),
                        onValueChange = {
                            lot1 = it.onlyInt()
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        imeAction = ImeAction.Next
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Est. Investment:", style = MaterialTheme.typography.bodySmall)
                    HSpacer(5.dp)
                    Text(additionInvested.orZero().rupiah(true), style = MaterialTheme.typography.bodySmall)
                }
                VSpacer(10.dp)
            }

            item {
                UseBrokerFee(
                    buyFee, sellFee,
                    withBrokerFee = useBrokerFee,
                    onCheckChanged = { useBrokerFee = it },
                    onClick = { showFeeDialog = true }
                )
            }

            item {
                VSpacer()
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = "Target Profit",
                        style = MaterialTheme.typography.titleMedium
                    )

                    CustomTab(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        items = targetProfitType2,
                        selectedItemIndex = selectedTarget,
                        tabWidth = 60.dp,
                        onClick = setSelectedTarget,
                    )
                }

                Row {
                    InputField(
                        modifier = Modifier.fillMaxWidth().weight(.6f),
                        label = "Value",
                        value = targetProfit1.asString(),
                        onValueChange = {
                            targetProfit1 = it.onlyInt()
                        },
                        keyboardType = KeyboardType.Decimal,
                        enabled = selectedTarget == 0,
                        visualTransformation = transformation,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    InputField(
                        modifier = Modifier.fillMaxWidth().padding(start = 10.dp).weight(.4f),
                        label = "Percentage",
                        value = targetPercentage.orEmpty(),
                        prefix = "%",
                        onValueChange = {
                            targetPercentage = it
                        },
                        enabled = selectedTarget == 1,
                        keyboardType = KeyboardType.Decimal,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                VSpacer()
                ButtonAndClear(
                    "Calculate",
                    enableButton = enableButton,
                    onClick = {
                        onCalculate.invoke(
                            AverageItem(initLot.orZero(), initAveragePrice.orZero().toFloat(), initInvested.orZero().toFloat()),
                            AverageItem(lot1.orZero(), price1.orZero().toFloat(), additionInvested.orZero().toFloat()),
                            useBrokerFee setWith buyFee,
                            targetProfit1.orZero(),
                            if (selectedTarget == 0) null else targetPercentage?.toFloatOrNull()
                        )
                    },
                    onClickIcon = {
                        onClear.invoke()
                        targetPercentage = null
                        targetProfit1 = null
                        initAveragePrice = null
                        initLot = null
                        price1 = null
                        lot1 = null
                    }
                )
            }
            
            item {
                VSpacer()
                RowItemTitle3("Price 1", "Lot", "Initial Investment")
                RowItemRes(initAveragePrice.orZero(), initLot.orZero(), initInvested.orZero())
                VSpacer(2.dp)
                RowItemTitle3("Price 2", "Lot", "Value")
                RowItemRes(price1.orZero(), lot1.orZero(), additionInvested.orZero())
                VSpacer(2.dp)
                if (avgResult != null) {
                    RowItemTitle3("Average Price", "Total Lot", "Total Investment")
                    RowItemRes(avgResult.average.toInt(), avgResult.lot, avgResult.value.toInt())
                }

                if (currentValue != null) {
                    VSpacer(10.dp)
                    ResultCard(
                        label1 = "Current Price",
                        value1 = currentValue.average.toInt().asString(),
                        value2 = currentValue.lot.asString(),
                        label2 = "Lot",
                        label3 = currentValue.value.rupiah(false),
                        value3 = (currentValue.value - (avgResult?.value ?: 0f)).rupiah(false)
                    )
                }
            }

            if (suggestSell != null) {
                item {
                    VSpacer(10.dp)
                    ResultCard2(
                        value1 = suggestSell.average.toInt().asString(),
                        value2 = suggestSell.value.rupiah(false),
                        value3 = suggestSell.pl.rupiah(false)
                    )
                }
            }
            if (suggestSell2 != null) {
                item {
                    VSpacer(10.dp)
                    Text("or", style = MaterialTheme.typography.titleSmall)
                    VSpacer(10.dp)
                    ResultCard2(
                        value1 = suggestSell2.average.toInt().asString(),
                        value2 = suggestSell2.value.rupiah(false),
                        value3 = suggestSell2.pl.rupiah(false)
                    )
                }
            }
        }
    }

    ChangeFeeModal(
        show = showFeeDialog,
        buyFee = buyFee,
        sellFee = sellFee,
        onDismiss = { showFeeDialog = false }
    ) { buy, sell ->
        println("ProfitStrategy: $buy, $sell")
        buyFee = buy
        sellFee = sell
        println("ProfitStrategy: $buyFee, $sellFee")
    }

}

@Composable
private fun ResultCard(
    label1: String = "Sell at",
    value1: String,
    label2: String = "Profit",
    value2: String,
    label3: String,
    value3: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(label1, style = MaterialTheme.typography.titleSmall)
            Text(value1, style = MaterialTheme.typography.titleLarge)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label2, style = MaterialTheme.typography.titleMedium)
            Text(value2, style = MaterialTheme.typography.titleSmall)
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End) {
            Text(label3, style = MaterialTheme.typography.titleMedium)
            Text(value3, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ResultCard2(
    label1: String = "Sell at",
    value1: String,
    value2: String,
    value3: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(label1, style = MaterialTheme.typography.titleSmall)
            Text(value1, style = MaterialTheme.typography.titleLarge)
        }
        Column(modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End) {
            Text(value2, style = MaterialTheme.typography.titleMedium)
            Text(value3, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RowItemTitle3(
    cl1: String = "Average Price", cl2: String = "Total Lot", cl3: String = "Total Investment",
    w1: Float = 0.1f, w2: Float = 0.1f, w3: Float = 0.2f
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f))
    ) {
        TableCell(cl1, weight = w1)
        TableCell(cl2, weight = w2)
        TableCell(cl3, weight = w3)
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
private fun Preview() {
    Content(rememberNavController(), onCalculate = { _,_,_,_,_-> },
        avgResult = AverageItem(1, 10f, 110f),
        currentValue = AverageItem(1, 10f, 110f),
        suggestSell = AverageItem(2, 3000f, 2000f),
        onClear = {})
}