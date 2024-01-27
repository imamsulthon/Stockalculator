package com.mamsky.stockalculator.android.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.R
import com.mamsky.stockalculator.android.screen.araarb.AutoRejectionPage
import com.mamsky.stockalculator.android.screen.average.AveragePricePage
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTickPage
import com.mamsky.stockalculator.android.screen.trading.TradingReturnPage

@Composable
fun AllCalculatorScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            MainScreenNavGraph(
                navController = navController
            )
        }
    }
}

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.AutoRejection
    ) {
        composable(Route.TradingReturn) {
            TradingReturnPage()
        }

        composable(Route.AveragePrice) {
            AveragePricePage()
        }

        composable(Route.AutoRejection) {
            AutoRejectionPage()
        }

        composable(Route.ProfitPerTick) {
            ProfitPerTickPage()
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.AutoRejection,
        BottomBarScreen.TradingReturn,
        BottomBarScreen.AveragePrice,
        BottomBarScreen.ProfitPerTick,
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

@Composable
fun AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.description } == true

    val background = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    else Color.Transparent
    val contentColor = if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.description) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = screen.icon),
//                imageVector = screen.icon,
                contentDescription = "icon",
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    color = contentColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

sealed class BottomBarScreen(
    val title: String,
    val icon: Int,
    val description: String,
    val page: Int
) {
    object TradingReturn : BottomBarScreen(
        title = "Trading Return",
        icon = R.drawable.trade,
        description = Route.TradingReturn,
        page = 0
    )

    object AveragePrice : BottomBarScreen(
        title = "Average Price",
        icon = R.drawable.ic_average,
        description = Route.AveragePrice,
        page = 1
    )

    object AutoRejection : BottomBarScreen(
        title = "Auto Rejection",
        icon = R.drawable.limited_offer,
        description = Route.AutoRejection,
        page = 2
    )

    object ProfitPerTick : BottomBarScreen(
        title = "Profit Per Tick",
        icon = R.drawable.investment,
        description = Route.ProfitPerTick,
        page = 3
    )

}

@Preview(showSystemUi = true)
@Composable
private fun HomeMainScreen_Preview() {
    AllCalculatorScreen(rememberNavController())
}