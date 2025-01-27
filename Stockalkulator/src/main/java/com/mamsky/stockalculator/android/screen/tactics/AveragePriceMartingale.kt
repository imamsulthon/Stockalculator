package com.mamsky.stockalculator.android.screen.tactics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.fee.ChangeFeeContent
import com.mamsky.stockalculator.android.screen.fee.UseBrokerFee
import com.mamsky.stockalculator.android.screen.fee.UseBrokerFee3
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTick
import com.mamsky.stockalculator.android.shared.ButtonAndClear
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputField3
import com.mamsky.stockalculator.android.shared.PageContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.data.BuyItemModel
import com.mamsky.stockalculator.engine.priceBuy
import com.mamsky.stockalculator.utils.asString
import com.mamsky.stockalculator.utils.downFold
import com.mamsky.stockalculator.utils.onlyInt
import com.mamsky.stockalculator.utils.orZero
import com.mamsky.stockalculator.utils.rupiah
import com.mamsky.stockalculator.utils.upFold

@Composable
fun MartingaleContent(
    navController: NavController,
    viewModel: MartingaleVM = hiltViewModel(key = "martingale"),
) {

    val avgModel by viewModel.avgModel.collectAsState()
    val result by viewModel.allItems.collectAsState()
    val average by viewModel.buyingAverage.collectAsState()
    val averageResult by viewModel.averageResult.collectAsState()

    Content(
        navController,
        events = viewModel::events,
        model = avgModel,
        result = result,
        averageItem = average,
        averageResult = averageResult,
        onClear = viewModel::clear,
        onCalculate = { init, startPrice, endPrice, foldPrice, startLot, recursion, fee, revertLot, uptrend ->
            viewModel.martingale(init, startPrice, endPrice, foldPrice, startLot, recursion, fee, revertLot, uptrend)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    navController: NavController,
    model: AvgDownModel = AvgDownModel(),
    events: (AvgFormEvent) -> Unit,
    result: List<BuyItemModel> = emptyList(),
    averageItem: AverageItem? = null,
    averageResult: AverageItem? = null,
    onClear: () -> Unit,
    onCalculate: (AverageItem, Int, Int, Int, Int, Int, Float, Boolean, Boolean) -> Unit
) {

    var initLot: Int? by remember { mutableStateOf(null) }
    var initAveragePrice: Int? by remember { mutableStateOf(null) }
    val initInvested: Int? by remember(initLot, initAveragePrice) {
        mutableIntStateOf(initAveragePrice.orZero().priceBuy(initLot ?: 0).toInt())
    }
    var startPrice: Int? by remember { mutableStateOf(null) }
    var endPrice: Int? by remember { mutableStateOf(null) }
    var foldPrice: Int? by remember { mutableStateOf(1) }
    var startLot: Int? by remember { mutableStateOf(1) }
    var lotFactor: Int? by remember { mutableStateOf(1) }
    var revertLot by remember { mutableStateOf(false) }
    var buyingUptrend by remember { mutableStateOf(false) }

    var useBroker by remember { mutableStateOf(false) }
    var showFeeDialog by remember { mutableStateOf(false) }
    var buyFee by remember { mutableFloatStateOf(0f) }

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            Text(text = "Initial Investment", style = MaterialTheme.typography.titleSmall)
            Row(modifier = Modifier.padding(vertical = 5.dp)) {
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Average Price",
                    value = initAveragePrice.asString(),
                    onValueChange = {
                        initAveragePrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
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
            Row {
                Text("Invested:")
                HSpacer(5.dp)
                Text(initInvested.orZero().rupiah(true))
            }
            VSpacer(10.dp)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Buying on Downtrend Prices", style = MaterialTheme.typography.titleSmall)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Uptrend")
                    HSpacer(5.dp)
                    Switch(
                        modifier = Modifier.padding(end = 6.dp),
                        checked = buyingUptrend,
                        onCheckedChange = {
                            buyingUptrend = it
                        }
                    )
                }
            }
            Row(modifier = Modifier.padding(vertical = 5.dp)) {
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Start Price",
                    value = startPrice.asString(),
                    onValueChange = {
                        startPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "End Price",
                    value = endPrice.asString(),
                    onValueChange = {
                        endPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField3(
                    modifier = Modifier.weight(1f).padding(start = 5.dp),
                    label = "Price Factor",
                    usePrefix = false,
                    useUpDown = true,
                    up = {
                        foldPrice = foldPrice.upFold()
                    },
                    down = {
                        foldPrice = foldPrice.downFold()
                    },
                    value = foldPrice.asString(),
                    onValueChange = {
                        foldPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    imeAction = ImeAction.Next
                )
            }
        }

        item {
            Row(modifier = Modifier.padding(bottom = 5.dp)) {

                InputField3(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Start Lot",
                    usePrefix = false,
                    useUpDown = true,
                    up = {
                        startLot = startLot.upFold()
                    },
                    down = {
                        startLot = startLot.downFold()
                    },
                    value = startLot.asString(),
                    onValueChange = {
                        startLot = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    imeAction = ImeAction.Next
                )
                InputField3(
                    modifier = Modifier.weight(1f),
                    label = "Recursion",
                    usePrefix = false,
                    useUpDown = true,
                    up = {
                        lotFactor = lotFactor.upFold()
                    },
                    down = {
                        lotFactor = lotFactor.downFold()
                    },
                    value = lotFactor.asString(),
                    onValueChange = {
                        lotFactor = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    imeAction = ImeAction.Next
                )
            }
            VSpacer(10.dp)
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UseBrokerFee3(
                    buyFee, buyFee,
                    withBrokerFee = useBroker,
                    onCheckChanged = { useBroker = it },
                    onClick = { showFeeDialog = true }
                )
                HSpacer(10.dp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier.padding(end = 6.dp),
                        checked = revertLot,
                        onCheckedChange = {
                            revertLot = it
                        }
                    )
                    Text("Revert Lot Sequence")
                    HSpacer()
                }
            }

            VSpacer(5.dp)
        }

        item {
            VSpacer(10.dp)
            ButtonAndClear(
                title = "Exercise",
                onClick = {
                    onCalculate.invoke(
                        AverageItem(initLot.orZero(), initAveragePrice.orZero().toFloat(), initInvested.orZero().toFloat()),
                        startPrice.orZero(),
                        endPrice.orZero(),
                        foldPrice.orZero(),
                        startLot.orZero(),
                        lotFactor.orZero(),
                        buyFee,
                        revertLot,
                        buyingUptrend,
                    )
                },
                onClickIcon = {
                    startPrice = 0
                    endPrice = 0
                    foldPrice = 0
                    startLot = 0
                    lotFactor = 0
                    initLot = 0
                    initAveragePrice = 0
                    onClear.invoke()
                },
            )
            VSpacer(10.dp)
        }

        if (result.isNotEmpty()) {
            item {
                Text("Suggestion Result", style = MaterialTheme.typography.titleSmall)
                VSpacer(5.dp)
                RowItemTitle()
            }

            items(items = result) {
                RowItemRes(it.price, it.lot, it.total)
            }
        }

        if (averageItem != null && result.isNotEmpty()) {
            item {
                Divider(modifier = Modifier.padding(vertical = 5.dp))
                RowItemRes(averageItem.average.toInt(), averageItem.lot, averageItem.value.toInt())
            }
        }

        if (averageResult != null) {
            item {
                VSpacer(10.dp)
                Text("Final Result", style = MaterialTheme.typography.titleSmall)
                RowItemTitle2()
                RowItemRes(averageResult.average.toInt(), averageResult.lot, averageResult.value.toInt())
                OutlinedButton(
                    onClick = {
                        navController.navigate(
                            ProfitPerTick.route(averageResult.lot, averageResult.average.toInt())
                        )
                    }
                ) {
                    Text("Profit/Tick")
                }
            }
        }

    }
    if (showFeeDialog) {
        ModalBottomSheet(
            onDismissRequest = { showFeeDialog = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            ChangeFeeContent { buy, _ ->
                buyFee = buy
                showFeeDialog = false
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun Preview() {
    PageContent("Martingale Content") {
        Content(model = AvgDownModel(), events = {},
            navController = rememberNavController(),
            onClear = {}
        ) { _, _, _, _, _, _, _, _, _ -> }
    }

}
