package com.mamsky.stockalculator.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.araarb.AraArbScreen
import com.mamsky.stockalculator.android.screen.average.AveragePriceScreen2
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTickScreen
import com.mamsky.stockalculator.android.screen.tactics.AverageDownPriceScreen
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
                averageDown = {
                    navController.navigate(Route.AverageDownPrice)
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
            AveragePriceScreen2(navController)
//            AveragePriceScreen(navController = navController)
        }

        composable(Route.AverageDownPrice) {
            AverageDownPriceScreen(navController = navController)
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
    const val AverageDownPrice = "average-price2"
    const val AutoRejection = "auto-rejection"
    const val ProfitPerTick = "profit-per-tick/{lots}/{price}"
    const val AllCalculator = "all-calculator"
}
