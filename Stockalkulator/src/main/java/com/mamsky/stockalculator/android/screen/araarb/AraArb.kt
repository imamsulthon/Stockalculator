package com.mamsky.stockalculator.android.screen.araarb

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mamsky.stockalculator.android.screen.onlyNumeric
import com.mamsky.stockalculator.android.screen.percentFormat
import com.mamsky.stockalculator.android.shared.Container
import com.mamsky.stockalculator.android.shared.HSpacer
import com.mamsky.stockalculator.android.shared.InputField
import com.mamsky.stockalculator.android.shared.VSpacer
import kotlin.math.abs

@Composable
fun AraArbScreen(
    viewModel: AraArbVM = hiltViewModel()
) {
    val items by viewModel.allItems.collectAsState()
    Content(
        items,
        calculate = { viewModel.calculate(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    list: List<AutoRejection>,
    calculate: (Int) -> Unit,
) {
    var price by remember { mutableIntStateOf(0) }
    val enableButton by remember(price) { mutableStateOf(price > 0) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ic_settings")
                },
                title = { Text(text = "Auto Rejection (ARA & ARB)") },
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
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
                            label = "Price",
                            value = price.toString(),
                            onValueChange = {
                                price = it.onlyNumeric()
                            },
                            imeAction = ImeAction.Next
                        )
                    }
                    VSpacer()
                }

                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { calculate.invoke(price) },
                        enabled = enableButton
                    ) { Text(text = "Calculate") }
                    VSpacer()
                }

                item {
                    list.forEach {
                        when (it.type) {
                            ARType.EQUAL.index -> {
                                VSpacer(5.dp)
                                EqualItem(data = it.price)
                                VSpacer(10.dp)
                            }
                            ARType.ARA.index -> {
                                AraItem(data = it)
                                VSpacer(5.dp)
                            }
                            ARType.ARB.index -> {
                                ArbItem(data = it)
                                VSpacer(5.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun log(m: String) = println("AraArb $m")

private val colorAra = Color.Blue.copy(alpha = 0.8f)
private val colorArb = Color.Red.copy(alpha = 0.6f)
private val colorEqual = Color.DarkGray.copy(alpha = 0.9f)

@Composable
private fun AraItem(data: AutoRejection) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue,
        ),
    ) {
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp)) {
            VSpacer(4.dp)
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Container(weight = 1f) {
                    Text(text = "ARA ${data.index}", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Rp ${data.price}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
                HSpacer(2.dp)
                Text(text = "+${data.increase}", modifier = Modifier.weight(1f), color = colorAra)
            }
            HSpacer(2.dp)
            Container(weight = 1.5f) {
                Text(text = "Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "${data.percentage.percentFormat()}%", style = MaterialTheme.typography.bodyMedium, color = colorAra)
            }
            HSpacer(2.dp)
            Container(weight = 1.5f) {
                Text(text = "Total Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "${data.totalPercentage.percentFormat()}%", style = MaterialTheme.typography.bodyMedium, color = colorAra)
            }
        }
    }
}

@Composable
private fun EqualItem(data: Int) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Green,
        ),
    ) {
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp)) {
            VSpacer(4.dp)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Container(weight = 1f) {
                    Text(text = "Price", style = MaterialTheme.typography.labelMedium)
                    Text(text = "Rp $data", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
                HSpacer(2.dp)
                Text(text = "+0", modifier = Modifier.weight(1f))
            }
            HSpacer(2.dp)
            Container(weight = 1f) {
                Text(text = "Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "0%", style = MaterialTheme.typography.bodyMedium, color = colorEqual)
            }
            HSpacer(2.dp)
            Container(weight = 1.2f) {
                Text(text = "Total Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "0%", style = MaterialTheme.typography.bodyMedium, color = colorEqual)
            }
        }
    }
}

@Composable
private fun ArbItem(data: AutoRejection) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Red,
        ),
    ) {
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp)
        ) {
            VSpacer(4.dp)
            Row(
                modifier = Modifier.weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Container(weight = 1f) {
                    Text(text = "ARB ${abs(data.index)}", style = MaterialTheme.typography.labelMedium)
                    Text(text = "Rp ${data.price}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
                HSpacer(2.dp)
                Text(text = "${data.increase}", modifier = Modifier.weight(1f), color = colorArb)
            }
            HSpacer(2.dp)
            Container(weight = 1.5f) {
                Text(text = "Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "${data.percentage.percentFormat()}%", style = MaterialTheme.typography.bodyMedium, color = colorArb)
            }
            "%.2f".format(data.totalPercentage)
            HSpacer(2.dp)
            Container(weight = 1.5f) {
                Text(text = "Total Percentage", style = MaterialTheme.typography.labelSmall)
                Text(text = "${data.totalPercentage.percentFormat()}%", style = MaterialTheme.typography.bodyMedium, color = colorArb)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AraArbScreen_Preview() {
    val list = RejectionEngineImpl().calculate(102)
    Content(list = list, calculate = {})
}