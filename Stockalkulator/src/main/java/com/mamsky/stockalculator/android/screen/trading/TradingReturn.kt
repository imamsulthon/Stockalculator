package com.mamsky.stockalculator.android.screen.trading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.fee.ChangeFeeContent
import com.mamsky.stockalculator.android.screen.fee.UseBrokerFee
import com.mamsky.stockalculator.android.screen.fee.default
import com.mamsky.stockalculator.android.shared.ButtonAndClear
import com.mamsky.stockalculator.android.shared.Container
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputField2
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.PageContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.android.shared.color
import com.mamsky.stockalculator.data.InputModel
import com.mamsky.stockalculator.data.ResultBuy
import com.mamsky.stockalculator.data.ResultCalculation
import com.mamsky.stockalculator.data.ResultSell
import com.mamsky.stockalculator.utils.asString
import com.mamsky.stockalculator.utils.onlyInt
import com.mamsky.stockalculator.utils.orZero
import com.mamsky.stockalculator.utils.percent
import com.mamsky.stockalculator.utils.percentFormat
import com.mamsky.stockalculator.utils.rupiah

private const val title = "Trading Return Calculator"

@Composable
fun TradingReturnScreen(
    viewModel: TradingReturnVM = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {

    val buyData by viewModel.buyData.collectAsState()
    val sellData by viewModel.sellData.collectAsState()
    val result by viewModel.result.collectAsState()

    MainContent(title, onBack = navController::popBackStack) {
        TradingReturn_Content(
            onCalculate = {
                viewModel.initCalculate(it)
            },
            buyResult = buyData,
            sellResult = sellData,
            resultCalculation = result,
            onClear = {}
        )
    }
}

@Composable
fun TradingReturnPage(
    viewModel: TradingReturnVM = hiltViewModel()
) {
    val buyData by viewModel.buyData.collectAsState()
    val sellData by viewModel.sellData.collectAsState()
    val result by viewModel.result.collectAsState()

    PageContent(title) {
        TradingReturn_Content(
            onCalculate = {
                viewModel.initCalculate(it)
            },
            buyResult = buyData,
            sellResult = sellData,
            resultCalculation = result,
            onClear = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradingReturn_Content(
    buyResult: ResultBuy = ResultBuy(),
    sellResult: ResultSell = ResultSell(),
    resultCalculation: ResultCalculation = ResultCalculation(),
    onCalculate: (InputModel) -> Unit,
    onClear: () -> Unit,
) {

    var showModal by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf(InputModel().default()) }

    var buyPrice: Int? by remember { mutableStateOf(0) }
    var sellPrice: Int? by remember { mutableStateOf(0) }
    var lot: Int? by remember { mutableStateOf(0) }
    var withBrokerFee by remember { mutableStateOf(false) }
    val brokerFeeSell by remember(model.feeForSell) { mutableFloatStateOf(model.feeForSell) }
    val brokerFeeBuy by remember(model.feeForBuy) { mutableFloatStateOf(model.feeForBuy) }
    val enableButton by remember(lot) { mutableStateOf(lot != null && lot!! > 0) }

    LazyColumn(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                InputField(
                    modifier = Modifier.weight(1f),
                    label = "Buy Price", value = buyPrice.asString(), onValueChange = {
                        buyPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField(
                    modifier = Modifier.weight(1f),
                    label = "Sell Price", value = sellPrice.asString(), onValueChange = {
                        sellPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField2(
                    modifier = Modifier.weight(1f),
                    usePrefix = false,
                    useUpDown = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    up = {
                        lot = lot.orZero() + 1
                    },
                    down = {
                        lot = lot.orZero() - 1
                    },
                    label = "Lot", value = lot.asString(), onValueChange = {
                        lot = it.onlyInt()
                    }
                )
            }
        }

        item {
            VSpacer(10.dp)
            UseBrokerFee(
                brokerFeeBuy = brokerFeeBuy,
                brokerFeeSell = brokerFeeSell,
                withBrokerFee = withBrokerFee,
                onClick = { showModal = true },
                onCheckChanged = { withBrokerFee = it }
            )
            VSpacer(10.dp)
            ButtonAndClear("Calculate",
                onClick = {
                    onCalculate.invoke(
                        InputModel(buyPrice.orZero(), sellPrice.orZero(), lot.orZero(), brokerFeeBuy, brokerFeeSell)
                    )
                },
                enableButton = enableButton,
                onClickIcon = onClear::invoke
            )
            VSpacer()
        }
        item {
            CalculationResult(resultCalculation)
            VSpacer(5.dp)
        }
        item {
            SellResult(sellResult)
            VSpacer(5.dp)
        }
        item {
            BuyResult(buyResult)
            VSpacer(5.dp)
        }
    }

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            ChangeFeeContent(
                onApply = { buy, sell ->
                    model = model.copy(
                        feeForBuy = buy,
                        feeForSell = sell
                    )
                    showModal = false
                }
            )
        }
    }

}

@Composable
fun CalculationResult(
    data: ResultCalculation,
) {
    CardContainer(title = "Calculation Result", onRemove = {}, borderColor = Color.Magenta,
        first = {
            Container {
                Text(text = "Status", style = MaterialTheme.typography.bodyMedium)
                Text(text = data.status, style = MaterialTheme.typography.bodySmall, color = data.profit.color())
                if (data.status.isNotEmpty())
                    Text(text = data.percentPL.percentFormat().percent(), style = MaterialTheme.typography.labelSmall, color = data.profit.color())
            }
            Container(Alignment.End) {
                Text(text = "Profit", style = MaterialTheme.typography.bodyMedium)
                Text(text = data.profit.rupiah(), style = MaterialTheme.typography.bodySmall, color = data.profit.color())
            }
        },
        second = {
            Container {
                Text(text = "Total Fee", style = MaterialTheme.typography.bodyMedium)
                Text(text = data.totalFee.rupiah(),
                    style = MaterialTheme.typography.bodySmall, color = data.totalFee.color())
            }
            Container(Alignment.End) {
                Text(text = "Net Profit", style = MaterialTheme.typography.bodyMedium)
                Text(text = data.netProfit.rupiah(), style = MaterialTheme.typography.bodySmall, color = data.netProfit.color())
            }
        }
    )
}

@Composable
private fun SellResult(
    data: ResultSell = ResultSell()
) {
    CardContainer(title = "Sell Result", onRemove = {}, borderColor = Color.Red,
        first = {
            Container {
                Text(text = "Sell Price", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${data.sellPrice.rupiah()} x ${data.lot} lot",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(text = "Sell Value", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = data.sellValue.rupiah(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        second = {
            Container {
                Text(
                    text = "Sell Fee (${data.fee.percentFormat()}%)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = data.sellFee.rupiah(true),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(
                    text = "Total Received",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = data.totalReceived.rupiah(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
private fun BuyResult(
    data: ResultBuy = ResultBuy()
) {
    CardContainer(title = "Buy Result", onRemove = {}, borderColor = Color.Green,
        first = {
            Container {
                Text(text = "Buy Price", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.price.rupiah()} x ${data.lot} lot",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(alignment = Alignment.End) {
                Text(text = "Buy Value", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = data.buyValue.rupiah(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        second = {
            Container {
                Text(
                    text = "Buy Fee (${data.fee.percentFormat()}%)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = data.buyFee.rupiah(true),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(text = "Total Paid", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = data.totalPaid.rupiah(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
internal fun CardContainer(
    title: String,
    onRemove: () -> Unit,
    borderColor: Color = Color.Blue,
    first: @Composable RowScope.() -> Unit,
    second: @Composable RowScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
        modifier = Modifier.padding(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }

        Divider(thickness = 1.dp, color = borderColor)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            first()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            second()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TradingReturnScreen_Preview() {
    TradingReturnScreen()
}