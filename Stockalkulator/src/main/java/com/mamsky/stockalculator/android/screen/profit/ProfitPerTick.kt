package com.mamsky.stockalculator.android.screen.profit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.Route
import com.mamsky.stockalculator.android.screen.fee.ChangeFeeContent
import com.mamsky.stockalculator.android.screen.fee.UseBrokerFee
import com.mamsky.stockalculator.android.shared.ButtonAndClear
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.InputField2
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.PageContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.android.shared.color
import com.mamsky.stockalculator.data.InputModel
import com.mamsky.stockalculator.data.ProfitBundle
import com.mamsky.stockalculator.data.ProfitInRow
import com.mamsky.stockalculator.domain.ProfitEngineImpl
import com.mamsky.stockalculator.utils.asString
import com.mamsky.stockalculator.utils.notZeroNull
import com.mamsky.stockalculator.utils.onlyInt
import com.mamsky.stockalculator.utils.percentFormat
import com.mamsky.stockalculator.utils.rupiah

private const val PAGE_TITLE = "Profit Per Tick"

object ProfitPerTick {

    const val PATH = Route.ProfitPerTick

    fun route(lots: Int, price: Int) = PATH
        .replace("{lots}", "$lots")
        .replace("{price}", "$price")
}

@Composable
fun ProfitPerTickScreen(
    viewModel: ProfitPerTickVM = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val list by viewModel.allItems.collectAsState()

    MainContent(PAGE_TITLE, onBack = navController::popBackStack) {
        ProfitPerTick_Content(
            list,
            onCalculate = { viewModel.calculate(it.buy, it.lot, it) },
            onClear = { viewModel.clear() },
        )
    }

}

@Composable
fun ProfitPerTickPage(
    viewModel: ProfitPerTickVM = hiltViewModel(),
    lots: Int?,
    price: Int?,
) {
    val list by viewModel.allItems.collectAsState()
    PageContent(PAGE_TITLE) {
        ProfitPerTick_Content(
            list,
            lots = lots,
            buy = price,
            onCalculate = { viewModel.calculate(it.buy, it.lot, it) },
            onClear = { viewModel.clear() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfitPerTick_Content(
    bundle: ProfitBundle = ProfitBundle(0),
    buy: Int? = null,
    lots: Int? = null,
    onCalculate: (InputModel) -> Unit,
    onClear: () -> Unit,
) {

    var showModal by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf(InputModel()) }

    var buyPrice: Int? by remember { mutableStateOf(buy) }
    var lot: Int? by remember { mutableStateOf(lots) }
    val enabledBuy by remember(buyPrice, lot) {
        mutableStateOf( buyPrice.notZeroNull() && lot.notZeroNull())
    }
    var withBrokerFee by remember { mutableStateOf(false) }
    val brokerFeeSell by remember(model.feeForSell) { mutableFloatStateOf(model.feeForSell) }
    val brokerFeeBuy by remember(model.feeForBuy) { mutableFloatStateOf(model.feeForBuy) }

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
                    label = "Price", value = buyPrice.asString(), onValueChange = {
                        buyPrice = it.onlyInt()
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField2(
                    usePrefix = false,
                    modifier = Modifier.weight(1f),
                    useUpDown = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = "Lot", value = lot.asString(), onValueChange = {
                        lot = it.onlyInt()
                    },
                    imeAction = ImeAction.Done
                )
            }
        }

        item {
            VSpacer(10.dp)
            UseBrokerFee(
                brokerFeeBuy,
                brokerFeeSell,
                withBrokerFee = withBrokerFee,
                onCheckChanged = { withBrokerFee = it },
                onClick = { showModal = true }
            )
            VSpacer(10.dp)
            ButtonAndClear(
                title = "Calculate",
                enableButton = enabledBuy,
                onClick = {
                    onCalculate.invoke(
                        InputModel().apply {
                            this.buy = buyPrice ?: 0
                            this.lot = lot ?: 0
                        }
                    )
                },
                onClickIcon = {
                    buyPrice = 0
                    lot = 0
                    onClear.invoke()
                },
            )
            VSpacer(10.dp)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Result",  style = MaterialTheme.typography.titleMedium)
                Text(text = "Initial Investment: ${bundle.initialValue.rupiah(true)}",
                    style = MaterialTheme.typography.labelSmall)
            }
        }

        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Price (Rp)", weight = cw1)
                TableCell(text = "Loss (%)", weight = cw2)
                TableCell(text = "Value (Rp)", weight = cw3)
                HSpacer(10.dp)
                TableCell(text = "Price (Rp)", weight = c4W)
                TableCell(text = "Gain (%)", weight = c5W)
                TableCell(text = "Value (Rp)", weight = c6W)
            }
        }

        items(bundle.list) {
            ItemRes(data = it)
            Divider(color = Color.LightGray)
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


private val cw1 = .2f // 30%
private val cw2 = .15f // 30%
private val cw3 = .2f // 40%
private val c4W = .2f
private val c5W = .15f
private val c6W = .2f

@Composable
fun ItemRes(
    data: ProfitInRow
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.Gray.copy(alpha = 0.2f))
    ) {
        TableCellItem(text = data.left.price.toString(), weight = cw1)
        TableCellItem(text = data.left.lossGain.percentFormat(), weight = cw2, color = data.left.lossGain.color())
        TableCellItem(text = data.left.value.rupiah(false, false), weight = cw3, color = data.left.value.color())
        HSpacer(10.dp)
        TableCellItem(text = data.right?.price.toString(), weight = cw1)
        TableCellItem(text = data.right?.lossGain?.percentFormat().orEmpty(), weight = cw2, color = data.right?.lossGain.color())
        TableCellItem(text = data.right?.value.rupiah(false, false), weight = cw3, color = data.right?.value.color())
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        fontSize = 9.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .border(1.dp, Color.Black.copy(alpha = .5f))
            .weight(weight)
            .padding(vertical = 8.dp, horizontal = 2.dp),
    )
}

@Composable
fun RowScope.TableCellItem(
    text: String,
    color: Color = Color.Black,
    weight: Float
) {
    Text(
        text = text,
        fontSize = 10.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 2.dp, vertical = 4.dp),
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfitPerTick_Preview() {
    val bundle = ProfitEngineImpl().calculate(120, 100, .0f, .0f)
    MainContent(PAGE_TITLE) {
        ProfitPerTick_Content(bundle, onCalculate = {}, onClear = {},)
    }
}


