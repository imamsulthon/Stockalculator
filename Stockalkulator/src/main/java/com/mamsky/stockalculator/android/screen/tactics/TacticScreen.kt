package com.mamsky.stockalculator.android.screen.tactics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.fee.ChangeFeeContent
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTick
import com.mamsky.stockalculator.android.screen.profit.TableCell
import com.mamsky.stockalculator.android.screen.profit.TableCellItem
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputField2
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.PageContent
import com.mamsky.stockalculator.android.shared.UseBrokerFee
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.data.AverageItem
import com.mamsky.stockalculator.data.BuyItemModel
import com.mamsky.stockalculator.domain.sheet
import com.mamsky.stockalculator.utils.asString
import com.mamsky.stockalculator.utils.onlyInt
import com.mamsky.stockalculator.utils.orZero
import com.mamsky.stockalculator.utils.rupiah

private const val TITLE = "Average Down Price"

@Composable
fun AverageDownPriceScreen(
    viewModel: AverageDowPriceVM = hiltViewModel(),
    navController: NavController = rememberNavController()
) {

    val result by viewModel.allItems.collectAsState()
    val average by viewModel.buyingAverage.collectAsState()
    val averageResult by viewModel.averageResult.collectAsState()

    MainContent(TITLE, onBack = navController::popBackStack) {
        Content(
            navController,
            result = result,
            averageItem = average,
            averageResult = averageResult,
            onClear = viewModel::clear,
            onCalculate = { init, minPrice, maxPrice, foldPrice, minLot, maxLot, lotFraction, fee ->
                viewModel.calculate(init, minPrice, maxPrice, foldPrice, minLot, maxLot, lotFraction, fee)
            },
        )
    }

}

@Composable
fun AverageDownPriceContent(
    viewModel: AverageDowPriceVM = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    val result by viewModel.allItems.collectAsState()
    val average by viewModel.buyingAverage.collectAsState()
    val averageResult by viewModel.averageResult.collectAsState()

    PageContent(TITLE) {
        Content(
            navController,
            result = result,
            averageItem = average,
            averageResult = averageResult,
            onClear = viewModel::clear,
            onCalculate = { init, minPrice, maxPrice, foldPrice, minLot, maxLot, lotFraction, fee ->
                viewModel.calculate(init, minPrice, maxPrice, foldPrice, minLot, maxLot, lotFraction, fee)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    navController: NavController,
    result: List<BuyItemModel> = emptyList(),
    averageItem: AverageItem? = null,
    averageResult: AverageItem? = null,
    onClear: () -> Unit,
    onCalculate: (AverageItem, Int, Int, Int, Int, Int, Int, Float) -> Unit,
) {
    var initLot: Int? by remember { mutableStateOf(0) }
    var initAveragePrice: Int? by remember { mutableStateOf(0) }
    val initInvested: Int? by remember(initLot, initAveragePrice) {
        mutableIntStateOf(initLot.orZero().sheet() * initAveragePrice.orZero())
    }
    var maxPrice: Int? by remember { mutableStateOf(0) }
    var minPrice: Int? by remember { mutableStateOf(0) }
    var targetPrice: Int? by remember { mutableStateOf(null) }
    var foldPrice: Int? by remember { mutableStateOf(0) }
    var highestLot: Int? by remember { mutableStateOf(0) }
    var lowestLot: Int? by remember { mutableStateOf(0) }
    var fractionFactor: Int? by remember { mutableStateOf(0) }

    var useBroker by remember { mutableStateOf(false) }
    var showFeeDialog by remember { mutableStateOf(false) }
    var buyFee by remember { mutableFloatStateOf(0f) }

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            Text(text = "Initial Investment")
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
                InputField(
                    modifier = Modifier.weight(1f).padding(start = 5.dp),
                    label = "Lot",
                    usePrefix = false,
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
            Text(text = "Buying on Downtrend Price")
            Row(modifier = Modifier.padding(vertical = 5.dp)) {
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Top Price",
                    value = maxPrice.asString(),
                    onValueChange = {
                        maxPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Bottom Price",
                    value = minPrice.asString(),
                    onValueChange = {
                        minPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField2(
                    modifier = Modifier.weight(1f),
                    label = "Price Factor",
                    usePrefix = false,
                    useUpDown = true,
                    up = {
                        foldPrice = foldPrice.orZero() + 1
                    },
                    down = {
                        foldPrice = foldPrice.orZero() - 1
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
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Lowest Lot",
                    usePrefix = false,
                    value = lowestLot.asString(),
                    onValueChange = {
                        lowestLot = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField(
                    modifier = Modifier.weight(1f).padding(end = 5.dp),
                    label = "Highest Lot",
                    usePrefix = false,
                    value = highestLot.asString(),
                    onValueChange = {
                        highestLot = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                InputField2(
                    modifier = Modifier.weight(1f),
                    label = "Lot Factor",
                    usePrefix = false,
                    useUpDown = true,
                    up = {
                        fractionFactor = fractionFactor.orZero() + 1
                    },
                    down = {
                        fractionFactor = fractionFactor.orZero() - 1
                    },
                    value = fractionFactor.asString(),
                    onValueChange = {
                        fractionFactor = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodySmall,
                    imeAction = ImeAction.Next
                )
            }
        }

        item {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                label = "Target Average",
                value = targetPrice.asString(),
                onValueChange = {
                    targetPrice = it.onlyInt()
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                imeAction = ImeAction.Next
            )
            VSpacer(10.dp)
        }

        item {
            UseBrokerFee(
                buyFee, buyFee,
                withBrokerFee = useBroker,
                onCheckChanged = { useBroker = it },
                onClick = { showFeeDialog = true }
            )
            VSpacer(10.dp)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    onClick = {
                        onCalculate.invoke(
                            AverageItem(initLot.orZero(), initAveragePrice.orZero().toFloat(), initInvested.orZero().toFloat()),
                            minPrice.orZero(),
                            maxPrice.orZero(),
                            foldPrice.orZero(),
                            lowestLot.orZero(),
                            highestLot.orZero(),
                            fractionFactor.orZero(),
                            buyFee,
                        )
                    }, enabled = true
                ) {
                    Text(text = "Exercise")
                }
                HSpacer(10.dp)
                OutlinedIconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        minPrice = 0
                        maxPrice = 0
                        foldPrice = 0
                        lowestLot = 0
                        highestLot = 0
                        fractionFactor = 0
                        initLot = 0
                        initAveragePrice = 0
                        onClear.invoke()
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "ic_delete")
                }
            }
            VSpacer(10.dp)
        }

        item {
            Text("Suggestion Result")
            VSpacer(5.dp)
            RowItemTitle()
        }

        items(items = result) {
            RowItemRes(it.price, it.lot, it.total)
        }

        if (averageItem != null) {
            item {
                Divider(modifier = Modifier.padding(vertical = 5.dp))
                RowItemRes(averageItem.average.toInt(), averageItem.lot, averageItem.value.toInt())
            }
        }

        if (averageResult != null) {
            item {
                VSpacer(10.dp)
                Text("Final Result")
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(color = Color.Gray.copy(alpha = 0.2f))
                ) {
                    TableCell("Average Price", weight = cw1)
                    TableCell("Total Lot", weight = cw2)
                    TableCell("Total Investment", weight = cw3)
                }
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

@Composable
private fun RowItemTitle(
    w1: Float = cw1, w2: Float = cw2, w3: Float = cw3,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f))
    ) {
        TableCell("Price", weight = w1)
        TableCell("Lot", weight = w2)
        TableCell("Total Price", weight = w3)
    }
}

@Composable
private fun RowItemRes(
    price: Int,
    lot: Int,
    total: Int,
) {
    Row {
        TableCellItem(price.rupiah(true, false), weight = cw1, color = Color.Blue)
        TableCellItem(lot.toString(), weight = cw2, color = Color.Blue)
        TableCellItem(total.rupiah(true), weight = cw3, color = Color.Blue)
    }
}

private val cw1 = .1f // 30%
private val cw2 = .1f // 30%
private val cw3 = .2f // 40%

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    PageContent("Average Down Price") {
        Content(
            rememberNavController(),
            onClear = {}
        ) { _, _, _, _, _, _, _, _ -> }
    }
}