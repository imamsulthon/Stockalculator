package com.mamsky.stockalculator.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.araarb.AraArbScreen
import com.mamsky.stockalculator.android.screen.average.AveragePrice
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTickScreen
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
                },
                profit = {
                    navController.navigate(Route.ProfitPerTick)
                },
                allCalculator = {
                    navController.navigate(Route.AllCalculator)
                }
            )
        }

        composable(Route.TradingReturn) {
            TradingReturnScreen(navController = navController)
        }

        composable(Route.AveragePrice) {
            AveragePrice(navController = navController)
        }

        composable(Route.AutoRejection) {
            AraArbScreen(navController = navController)
        }

        composable(Route.ProfitPerTick) {
            ProfitPerTickScreen(navController = navController)
        }

        composable(Route.AllCalculator) {
            AllCalculatorScreen()
        }
    }
}

object Route {
    const val Home = "home"
    const val TradingReturn = "trading-return"
    const val AveragePrice = "average-price"
    const val AutoRejection = "auto-rejection"
    const val ProfitPerTick = "profit-per-tick"
    const val AllCalculator = "all-calculator"
}
