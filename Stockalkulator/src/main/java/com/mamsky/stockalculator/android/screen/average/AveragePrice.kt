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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mamsky.stockalculator.android.screen.onlyNumeric
import com.mamsky.stockalculator.android.screen.trading.InputModel
import com.mamsky.stockalculator.android.shared.Container
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.VSpacer


@Composable
fun AveragePrice(
    viewModel: AveragePriceVM = hiltViewModel()
) {
    val items by viewModel.allItems.collectAsState()
    val averageItem by viewModel.average.collectAsState()

    Content(
        list = items,
        averageItem = averageItem,
        onCalculate = {
            viewModel.buy(it.buy, it.lot)
        },
        onRemove = viewModel::remove
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    list: List<BuyItemModel> = listOf(),
    averageItem: AverageItem = AverageItem(0, 0f, 0f),
    onCalculate: (InputModel) -> Unit,
    onRemove: (BuyItemModel) -> Unit,
) {
    var buyPrice by remember {
        mutableIntStateOf(0)
    }
    var lot by remember {
        mutableIntStateOf(0)
    }

    val enabledBuy by remember(buyPrice, lot) {
        mutableStateOf(buyPrice > 0 && lot > 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ic_settings")
                },
                title = {
                    Text(text = "Average Price")
                },
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
                        label = "Price", value = buyPrice.toString(), onValueChange = {
                            buyPrice = it.onlyNumeric()
                        },
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    InputField(
                        usePrefix = false,
                        modifier = Modifier.weight(1f),
                        label = "Lot", value = lot.toString(), onValueChange = {
                            lot = it.onlyNumeric()
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
                    Text(text = "Value: Rp ${averageItem.average}")
                    Spacer(modifier = Modifier.width(10.dp))
                }
                VSpacer(10.dp)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onCalculate.invoke(
                            InputModel().apply {
                                this.buy = buyPrice
                                this.lot = lot
                            }
                        )
                    }, enabled = enabledBuy
                ) {
                    Text(text = "Buy")
                }
                VSpacer()
            }

            item {
                Card(
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Green,
                    ),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        text = "Result", style = MaterialTheme.typography.titleLarge)
                    Divider(thickness = 1.dp, color = Color.Gray)
                    Row(modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 5.dp)) {
                        Container {
                            Text(text = "Buy", style = MaterialTheme.typography.labelMedium)
                            Text(text = "Rp ${averageItem.average}", style = MaterialTheme.typography.bodySmall)
                        }
                        HSpacer(4.dp)
                        Container {
                            Text(text = "Lot", style = MaterialTheme.typography.labelMedium)
                            Text(text = "${averageItem.lot}", style = MaterialTheme.typography.bodySmall)
                        }
                        HSpacer(4.dp)
                        Container {
                            Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                            Text(text = "Rp ${averageItem.value}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                VSpacer()
            }

            if (list.isNotEmpty()) {
                item {
                    Text(text = "Transaction History", style = MaterialTheme.typography.titleMedium)
                    VSpacer(5.dp)
                }
            }


            items(list) {
                BuyItem(it, onRemove = { onRemove.invoke(it) })
                VSpacer(10.dp)
            }
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
        Divider(modifier = Modifier.padding(vertical = 2.dp), thickness = 1.dp, color = Color.Gray)
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp)) {
            Container {
                Text(text = "Buy", style = MaterialTheme.typography.labelMedium)
                Text(text = "Rp ${data.price}", style = MaterialTheme.typography.bodySmall)
            }
            HSpacer(4.dp)
            Container {
                Text(text = "Lot", style = MaterialTheme.typography.labelMedium)
                Text(text = "${data.lot}", style = MaterialTheme.typography.bodySmall)
            }
            HSpacer(4.dp)
            Container {
                Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                Text(text = "Rp ${data.total}", style = MaterialTheme.typography.bodySmall)
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
    Content(list = list, onCalculate = {}, onRemove = {})
}