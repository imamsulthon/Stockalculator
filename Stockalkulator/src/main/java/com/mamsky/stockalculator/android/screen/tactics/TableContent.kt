package com.mamsky.stockalculator.android.screen.tactics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mamsky.stockalculator.android.screen.profit.TableCell
import com.mamsky.stockalculator.android.screen.profit.TableCellItem
import com.mamsky.stockalculator.android.shared.CustomTab
import com.mamsky.stockalculator.utils.rupiah

@Composable
fun TrendOption(selectedItemIndex: Int, onChange: (Int) -> Unit) {
    CustomTab(
        modifier = Modifier.wrapContentWidth(),
        tabWidth = 110.dp,
        selectedItemIndex = selectedItemIndex,
        items = listOf("Downtrend", "Uptrend")
    ) { index ->
        onChange.invoke(index)
    }
}

@Composable
fun RowItemTitle(
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
fun RowItemTitle2(
    w1: Float = cw1, w2: Float = cw2, w3: Float = cw3,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f))
    ) {
        TableCell("Average Price", weight = w1)
        TableCell("Total Lot", weight = w2)
        TableCell("Total Investment", weight = w3)
    }
}

@Composable
fun RowItemRes(
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