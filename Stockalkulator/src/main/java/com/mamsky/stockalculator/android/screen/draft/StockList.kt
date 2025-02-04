package com.mamsky.stockalculator.android.screen.draft

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.shared.MainContent
import com.mamsky.stockalculator.android.shared.VSpacer
import com.mamsky.stockalculator.utils.rupiah


@Composable
fun MyStocksScreen(
    navController: NavController = rememberNavController(),
    viewModel: StockListVM = hiltViewModel()
) {
    val list by viewModel.allItems.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAll()
    }
    Content(navController, list)
}

@Composable
private fun Content(
    navController: NavController = rememberNavController(),
    list: List<StockEntity>
) {
    MainContent(title = "Stocks Draft", onBack = navController::popBackStack) {
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            if (list.isEmpty()) {
                item {
                    Text("No Data", style = MaterialTheme.typography.titleMedium)
                }
                return@LazyColumn
            }
            items(items = list) {
                ItemsContent(it)
                VSpacer(10.dp)
            }
        }
    }
}

@Composable
private fun ItemsContent(item: StockEntity) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .border(border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(5.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(item.code, style = MaterialTheme.typography.titleMedium)
            Text(item.companyName, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Column(modifier = Modifier.weight(.4f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("PER", style = MaterialTheme.typography.bodySmall)
            Text(item.per.toString(), style = MaterialTheme.typography.bodySmall)
        }
        Column(modifier = Modifier.weight(.4f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("PBV", style = MaterialTheme.typography.bodySmall)
            Text(item.per.toString(), style = MaterialTheme.typography.bodySmall)
        }
        Text(modifier = Modifier.weight(1f), textAlign = TextAlign.End,
            text = item.currentPrice?.toInt().rupiah(false, false), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
@Preview
private fun Preview() {
    Content(list = getAll())
}

private fun getAll(): List<StockEntity> {
    val list = mutableListOf<StockEntity>()

    for (i in 1..10) {
        val d = StockEntity("ABCD", "PT Bukit Asam $i", "Energi dan Gas", "desc",
            "Gas", "Coal", 0.0, 0.0, 0, 0.0)
        list.add(d)
    }
    return list
}