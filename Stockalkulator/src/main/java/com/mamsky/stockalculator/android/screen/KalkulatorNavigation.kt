package com.mamsky.stockalculator.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.araarb.AraArbScreen
import com.mamsky.stockalculator.android.screen.average.AveragePrice
import com.mamsky.stockalculator.android.screen.trading.TradingReturnScreen


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
            HomeScreen(
                trading = {
                    navController.navigate(Route.TradingReturn)
                },
                average = {
                    navController.navigate(Route.AveragePrice)
                },
                araArb = {
                    navController.navigate(Route.AutoRejection)
                }
            )
        }

        composable(Route.TradingReturn) {
            TradingReturnScreen()
        }

        composable(Route.AveragePrice) {
            AveragePrice()
        }

        composable(Route.AutoRejection) {
            AraArbScreen()
        }
    }
}

object Route {
    const val Home = "home"
    const val TradingReturn = "trading-return"
    const val AveragePrice = "average-price"
    const val AutoRejection = "auto-rejection"
}
