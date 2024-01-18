package com.mamsky.stockalculator.android.screen.trading

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mamsky.stockalculator.android.screen.onlyNumeric
import com.mamsky.stockalculator.android.shared.Container
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.VSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingReturnScreen(
    viewModel: TradingReturnVM = hiltViewModel()
) {

    var showModal by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf(InputModel()) }
    val buyData by viewModel.buyData.collectAsState()
    val sellData by viewModel.sellData.collectAsState()
    val result by viewModel.result.collectAsState()

    Content(
        initInput = model,
        onCalculate = {
            viewModel.initCalculate(it)
        },
        buyResult = buyData,
        sellResult = sellData,
        resultCalculation = result,
        changeFee = { showModal = true }
    )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    initInput: InputModel = InputModel(),
    buyResult: ResultBuy = ResultBuy(),
    sellResult: ResultSell = ResultSell(),
    resultCalculation: ResultCalculation = ResultCalculation(),
    onCalculate: (InputModel) -> Unit,
    changeFee: () -> Unit,
) {
    var buyPrice by remember { mutableIntStateOf(0) }
    var sellPrice by remember { mutableIntStateOf(0) }
    var lot by remember { mutableIntStateOf(0) }
    var withBrokerFee by remember { mutableStateOf(false) }
    val brokerFeeSell by remember(initInput.feeForSell) { mutableFloatStateOf(initInput.feeForSell) }
    val brokerFeeBuy by remember(initInput.feeForBuy) { mutableFloatStateOf(initInput.feeForBuy) }
    val enableButton by remember(lot) { mutableStateOf(lot > 0) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ic_settings")
                },
                title = {
                    Text(text = "Trading Return Calculator")
                },
                actions = {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "ic_settings")
                }
            )
        }
    ) { p ->
        LazyColumn(
            modifier = Modifier
                .padding(p)
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
                        label = "Buy Price", value = buyPrice.toString(), onValueChange = {
                            buyPrice = it.onlyNumeric()
                        },
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    InputField(
                        modifier = Modifier.weight(1f),
                        label = "Sell Price", value = sellPrice.toString(), onValueChange = {
                            sellPrice = it.onlyNumeric()
                        },
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    InputField(
                        modifier = Modifier.weight(1f),
                        usePrefix = false,
                        label = "Lot", value = lot.toString(), onValueChange = {
                            lot = it.toInt()
                        })
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier.height(40.dp),
                        checked = withBrokerFee,
                        onCheckedChange = {
                            withBrokerFee = it
                        },
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Use Broker fee?")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Broker fee: Buy $brokerFeeBuy%, Sell $brokerFeeSell%")
                    Spacer(modifier = Modifier.width(10.dp))
                    OutlinedIconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = { changeFee.invoke() },
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = "ic_settings"
                        )
                    }
                }
                VSpacer(10.dp)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCalculate.invoke(
                            InputModel(buyPrice, sellPrice, lot, brokerFeeBuy, brokerFeeSell)
                        )
                    }, enabled = enableButton,
                ) {
                    Text(text = "Calculate")
                }
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
    }
}

@Composable
fun CalculationResult(
    data: ResultCalculation,
) {
    CardContainer(title = "Calculation Result", onRemove = {},
        first = {
            Container {
                Text(text = "Status", style = MaterialTheme.typography.bodyMedium)
                Text(text = data.status, style = MaterialTheme.typography.bodySmall)
            }
            Container(Alignment.End) {
                Text(text = "Profit", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Rp ${data.profit}", style = MaterialTheme.typography.bodySmall)
            }
        },
        second = {
            Container {
                Text(text = "Total Fee", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Rp ${data.totalFee}", style = MaterialTheme.typography.bodySmall)
            }
            Container(Alignment.End) {
                Text(text = "Net Profit", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Rp ${data.netProfit}", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Composable
private fun SellResult(
    data: ResultSell = ResultSell()
) {
    CardContainer(title = "Sell Result", onRemove = {},
        first = {
            Container {
                Text(text = "Sell Price", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.sellPrice} x ${data.lot} lot",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(text = "Sell Value", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.sellValue}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        second = {
            Container {
                Text(
                    text = "Sell Fee (0%)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${data.fee}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(
                    text = "Total Received",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${data.totalReceived}",
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
    CardContainer(title = "Buy Result", onRemove = {},
        first = {
            Container {
                Text(text = "Buy Price", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.price} x ${data.lot} lot",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(alignment = Alignment.End) {
                Text(text = "Buy Value", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.buyValue}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        second = {
            Container {
                Text(
                    text = "Buy Fee (${data.fee}%)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Rp ${(data.price * data.fee)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Container(Alignment.End) {
                Text(text = "Total Paid", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Rp ${data.totalPaid}",
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
    first: @Composable RowScope.() -> Unit,
    second: @Composable RowScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue,
        ),
        modifier = Modifier.padding(5.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium)
            OutlinedIconButton(
                modifier = Modifier.size(20.dp),
                onClick = onRemove::invoke,
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "ic_settings"
                )
            }
        }

        Divider(thickness = 1.dp, color = Color.Gray)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            first()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp).padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            second()
        }
    }
}

@Composable
fun ChangeFeeContent(
    buy: Float? = 0f,
    sell: Float? = 0f,
    onApply: (Float, Float) -> Unit
) {
    var buyFee by remember {
        mutableStateOf(buy)
    }
    var sellFee by remember {
        mutableStateOf(sell)
    }
    Surface(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 30.dp),
    ) {
        Column {
            Text(text = "Set Fee", style = MaterialTheme.typography.titleLarge)
            VSpacer()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InputField(
                    modifier = Modifier.weight(1f),
                    label = "Buy Fee", value = buyFee.toString(), onValueChange = {
                        buyFee = it.toFloat()
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField(modifier = Modifier.weight(1f),
                    label = "Sell Fee", value = sellFee.toString(), onValueChange = {
                        sellFee = it.toFloat()
                    }
                )
            }
            VSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    onApply.invoke(buyFee ?: 0f, sellFee ?: 0f)
                }) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TradingReturnScreen_Preview() {
    TradingReturnScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChangeFeeContent_Preview() {
    ModalBottomSheet(
        onDismissRequest = { /*TODO*/ },
        sheetState = rememberStandardBottomSheetState()
    ) {
        ChangeFeeContent(
            onApply = { b, s ->
            }
        )
    }
}