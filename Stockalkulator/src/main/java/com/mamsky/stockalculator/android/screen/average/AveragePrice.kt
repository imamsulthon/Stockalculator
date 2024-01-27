package com.mamsky.stockalculator.android.screen.average

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.mamsky.stockalculator.android.screen.asString
import com.mamsky.stockalculator.android.screen.onlyInt
import com.mamsky.stockalculator.android.screen.rupiah
import com.mamsky.stockalculator.android.screen.trading.InputModel
import com.mamsky.stockalculator.android.shared.ButtonIcon
import com.mamsky.stockalculator.android.shared.Container
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.PageContent
import com.mamsky.stockalculator.android.shared.VSpacer

@Composable
fun AveragePrice(
    viewModel: AveragePriceVM = hiltViewModel(),
    navController: NavController = rememberNavController(),
) {
    val items by viewModel.allItems.collectAsState()
    val averageItem by viewModel.average.collectAsState()

    MainContent("Average Price", onBack = navController::popBackStack) {
        AveragePrice_Content(
            list = items,
            averageItem = averageItem,
            onCalculate = { viewModel.buy(it.buy, it.lot) },
            onClear = viewModel::clear,
            onRemove = viewModel::remove
        )
    }
}

@Composable
fun AveragePricePage(
    viewModel: AveragePriceVM = hiltViewModel()
) {
    val items by viewModel.allItems.collectAsState()
    val averageItem by viewModel.average.collectAsState()

    PageContent("Average Price") {
        AveragePrice_Content(
            list = items,
            averageItem = averageItem,
            onCalculate = { viewModel.buy(it.buy, it.lot) },
            onClear = viewModel::clear,
            onRemove = viewModel::remove
        )
    }
}

@Composable
fun AveragePrice_Content(
    list: List<BuyItemModel> = listOf(),
    averageItem: AverageItem = AverageItem(0, 0f, 0f),
    onCalculate: (InputModel) -> Unit,
    onClear: () -> Unit,
    onRemove: (BuyItemModel) -> Unit,
) {
    var buyPrice: Int? by remember { mutableStateOf(0) }
    var lot: Int? by remember { mutableStateOf(0) }
    val enabledBuy by remember(buyPrice, lot) {
        mutableStateOf(buyPrice!! > 0 && lot!! > 0)
    }
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
                InputField(
                    usePrefix = false,
                    modifier = Modifier.weight(1f),
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Value: Rp ${averageItem.average.rupiah()}")
                Spacer(modifier = Modifier.width(10.dp))
            }
            VSpacer(10.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    onClick = {
                        onCalculate.invoke(
                            InputModel().apply {
                                this.buy = buyPrice ?: 0
                                this.lot = lot ?: 0
                            }
                        )
                    }, enabled = enabledBuy
                ) {
                    Text(text = "Buy")
                }
                HSpacer(10.dp)
                OutlinedIconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        buyPrice = 0
                        lot = 0
                        onClear.invoke()
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "ic_delete")
                }
            }

            VSpacer()
        }

        item {
            Card(
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Magenta,
                ),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    text = "Result", style = MaterialTheme.typography.titleMedium)
                Divider(thickness = 1.dp, color = Color.Magenta)
                Row(modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Container {
                        Text(text = "Buy", style = MaterialTheme.typography.labelMedium)
                        Text(text = "Rp ${averageItem.average.rupiah()}", style = MaterialTheme.typography.bodySmall)
                    }
                    HSpacer(4.dp)
                    Container {
                        Text(text = "Lot", style = MaterialTheme.typography.labelMedium)
                        Text(text = "${averageItem.lot}", style = MaterialTheme.typography.bodySmall)
                    }
                    HSpacer(4.dp)
                    Container {
                        Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                        Text(text = "Rp ${averageItem.value.rupiah()}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            VSpacer()
        }

        if (list.isNotEmpty()) {
            item {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Transaction History", style = MaterialTheme.typography.titleMedium)
                    ButtonIcon(icon = Icons.Outlined.Star) {

                    }
                }
                VSpacer(5.dp)
            }
        }

        items(list) {
            BuyItem(it, onRemove = { onRemove.invoke(it) })
            VSpacer(10.dp)
        }
    }
}

@Composable
private fun BuyItem(data: BuyItemModel, onRemove: () -> Unit) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue,
        ),
    ) {
        IconButton(
            modifier = Modifier
                .padding(5.dp)
                .size(20.dp)
                .align(Alignment.End),
            onClick = { onRemove.invoke() }
        ) {
            Image(imageVector = Icons.Default.Close, contentDescription = "ic_delete")
        }
        Divider(modifier = Modifier.padding(vertical = 2.dp), thickness = 1.dp, color = Color.Blue)
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Container {
                Text(text = "Buy", style = MaterialTheme.typography.labelMedium)
                Text(text = "Rp ${data.price.rupiah()}", style = MaterialTheme.typography.bodySmall)
            }
            HSpacer(4.dp)
            Container {
                Text(text = "Lot", style = MaterialTheme.typography.labelMedium)
                Text(text = "${data.lot}", style = MaterialTheme.typography.bodySmall)
            }
            HSpacer(4.dp)
            Container {
                Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                Text(text = "Rp ${data.total.rupiah()}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun AveragePrice_Preview() {
    val list = listOf(
        BuyItemModel(1, 10, 10),
        BuyItemModel(1, 20, 10),
        BuyItemModel(1, 30, 100)
    )
    MainContent("AveragePrice_Preview") {
        AveragePrice_Content(list = list, onCalculate = {}, onClear = {}, onRemove = {})
    }
}