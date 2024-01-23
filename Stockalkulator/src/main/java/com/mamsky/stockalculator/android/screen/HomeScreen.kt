package com.mamsky.stockalculator.android.screen

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mamsky.stockalculator.android.shared.VSpacer


@Composable
fun HomeScreen(
    trading: () -> Unit,
    average: () -> Unit,
    araArb: () -> Unit,
    profit: () -> Unit,
) {
    HomeScreenContent(
        trading = trading::invoke,
        average = average::invoke,
        araArb = araArb::invoke,
        profit = profit::invoke
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    trading: () -> Unit,
    average: () -> Unit,
    araArb: () -> Unit,
    profit: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Stock Calculator")
            })
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(10.dp).widthIn(max = 180.dp)) {
                MenuItem(title = "Trading Return") {
                    trading.invoke()
                }
                VSpacer(10.dp)
                MenuItem(title = "Average Price") {
                    average.invoke()
                }
                VSpacer(10.dp)
                MenuItem(title = "ARA & ARB") {
                    araArb.invoke()
                }
                VSpacer(10.dp)
                MenuItem(title = "Profit Per Tick") {
                    profit.invoke()
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuItem(
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        onClick = { onClick.invoke() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),
                text = title, style = MaterialTheme.typography.titleMedium
            )
            Image(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "ic_forward")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreen_Preview() {
    HomeScreen(trading = {}, araArb = {}, average = {}, profit = {})
}