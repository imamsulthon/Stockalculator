package com.mamsky.stockalculator.android.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainContent(
    title: String = "",
    onBack: (() -> Unit)? = null,
    contents: @Composable () -> Unit,
) {
    Scaffold(
        topBar = { MyAppBar(title) { onBack?.invoke() } },
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            contents()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageContent(
    title: String = "",
    contents: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
             TopAppBar(title = { Text(text = title) })
        },
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            contents()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomNavCustom() {
    val items = listOf(
        "String1", "String-2",
        "String3", "String-4"
    )
    BottomNavigation {
        items.forEach { text ->
            BottomNavigationItem(
                icon = {
                    BadgedBox(badge = { Badge { Text("6") } }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite"
                        )
                    }

                },
                label = { Text(text = text) },
                selected = true,
                onClick = {}
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainContent_Preview() {
    MainContent("Example title") {

    }
}