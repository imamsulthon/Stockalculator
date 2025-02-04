package com.mamsky.stockalculator.android.screen.fee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.data.InputModel
import com.mamsky.stockalculator.utils.asString

object ConstantFee {
    const val BUY = 0.15f
    const val SELL = 0.20f
}

fun InputModel.default(): InputModel {
    this.feeForBuy = ConstantFee.BUY
    this.feeForSell = ConstantFee.SELL
    return this
}

infix fun Boolean.setWith(fee: Float): Float {
    return if (this) fee else 0f
}

@Composable
fun rememberBuyFeeDefault() = remember { mutableFloatStateOf(ConstantFee.BUY) }

@Composable
fun rememberSellFeeDefault() = remember { mutableFloatStateOf(ConstantFee.SELL) }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeFeeModal(
    show: Boolean,
    buyFee: Float,
    sellFee: Float,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
    onApply: (Float, Float) -> Unit
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss.invoke() },
            sheetState = sheetState
        ) {
            ChangeFeeContent(buyFee, sellFee, onCancel = onDismiss::invoke) { buy, sell ->
                onApply.invoke(buy, sell)
                onDismiss.invoke()
            }
        }
    }
}

@Composable
fun ChangeFeeContent(
    buy: Float? = ConstantFee.BUY,
    sell: Float? = ConstantFee.SELL,
    onCancel: () -> Unit,
    onApply: (Float, Float) -> Unit
) {
    var buyFee: String? by remember { mutableStateOf(buy.asString()) }
    var sellFee: String? by remember { mutableStateOf(sell.asString()) }

    Surface(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 30.dp),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "ic_settings")
                HSpacer(10.dp)
                Text(text = "Set Fee", style = MaterialTheme.typography.titleLarge)
            }
            VSpacer()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InputField(
                    modifier = Modifier.weight(1f),
                    label = "Buy Fee", value = buyFee.orEmpty(), onValueChange = {
                        buyFee = it
                    },
                    prefix = "%",
                    keyboardType = KeyboardType.Decimal
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField(modifier = Modifier.weight(1f),
                    label = "Sell Fee", value = sellFee.orEmpty(),
                    onValueChange = {
                        sellFee = it
                    },
                    prefix = "%",
                    keyboardType = KeyboardType.Decimal
                )
            }
            VSpacer()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onCancel::invoke) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    onApply.invoke(
                        buyFee?.toFloatOrNull() ?: 0f,
                        sellFee?.toFloatOrNull() ?: 0f
                    )
                }) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun UseBrokerFee(
    brokerFeeBuy: Float = 0.15f,
    brokerFeeSell: Float = 0.20f,
    withBrokerFee: Boolean = false,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Switch(
            modifier = Modifier.height(4.dp),
            checked = withBrokerFee,
            onCheckedChange = onCheckChanged::invoke,
        )
        HSpacer(5.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Broker fee? Buy $brokerFeeBuy%, Sell $brokerFeeSell%")
            HSpacer(5.dp)
            OutlinedIconButton(
                modifier = Modifier.size(20.dp),
                onClick = onClick::invoke,
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "ic_settings"
                )
            }
        }
    }
}

@Composable
fun UseBrokerFee3(
    brokerFeeBuy: Float = 0.15f,
    brokerFeeSell: Float = 0.20f,
    withBrokerFee: Boolean = false,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    @Composable
    fun switch() {
        Switch(
            modifier = Modifier.height(4.dp),
            checked = withBrokerFee,
            onCheckedChange = onCheckChanged::invoke,
        )
    }

    @Composable
    fun desc() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                modifier = Modifier.size(20.dp),
                onClick = onClick::invoke,
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "ic_settings"
                )
            }
            HSpacer(5.dp)
            Column {
                Text(text = "Broker fee?")
                Text(text = "Buy $brokerFeeBuy%, Sell $brokerFeeSell%", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (alignment == Alignment.Start) {
            switch()
            HSpacer(5.dp)
            desc()
        } else {
            desc()
            HSpacer(5.dp)
            switch()
        }
    }
}

@Composable
fun UseBrokerFee2(
    brokerFeeBuy: Float,
    brokerFeeSell: Float,
    withBrokerFee: Boolean = false,
    onCheckChanged: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Checkbox(
            checked = withBrokerFee,
            onCheckedChange = onCheckChanged::invoke
        )
        HSpacer(5.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Broker fee? Buy $brokerFeeBuy%, Sell $brokerFeeSell%")
            HSpacer(5.dp)
            OutlinedIconButton(
                modifier = Modifier.size(20.dp),
                onClick = onClick::invoke,
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "ic_settings"
                )
            }
        }
    }
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
            onApply = { _, _ -> }, onCancel = {}
        )
    }
}