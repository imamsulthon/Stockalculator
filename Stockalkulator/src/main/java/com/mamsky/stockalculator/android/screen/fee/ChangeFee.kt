package com.mamsky.stockalculator.android.screen.fee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mamsky.stockalculator.android.screen.asString
import com.mamsky.stockalculator.android.screen.onlyFloat
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.VSpacer


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
                    label = "Buy Fee", value = buyFee.asString(), onValueChange = {
                        buyFee = it.toFloat()
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                InputField(modifier = Modifier.weight(1f),
                    label = "Sell Fee", value = sellFee.asString(), onValueChange = {
                        sellFee = it.onlyFloat()
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