package com.mamsky.stockalculator.android.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mamsky.stockalculator.android.screen.araarb.AraArbScreen
import com.mamsky.stockalculator.android.screen.average.AveragePriceScreen
import com.mamsky.stockalculator.android.screen.average.AverageStrategyScreen2
import com.mamsky.stockalculator.android.screen.draft.CreateStockScreen
import com.mamsky.stockalculator.android.screen.draft.MyStocksScreen
import com.mamsky.stockalculator.android.screen.profit.ProfitPerTickScreen
import com.mamsky.stockalculator.android.screen.profit.ProfitStrategyScreen
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
                    navController.navigate(Route.AverageStrategy)
                },
                araArb = {
                    navController.navigate(Route.AutoRejection)
                },
                profit = {
                    navController.navigate(Route.ProfitPerTick)
                },
                allCalculator = {
                    navController.navigate(Route.AllCalculator)
                },
                createStock = {
                    navController.navigate(Route.CreateStock)
                },
                stockList = {
                    navController.navigate(Route.StockList)
                },
                profitStrategy = {
                    navController.navigate(Route.ProfitStrategy)
                },
            )
        }

        composable(Route.TradingReturn) {
            TradingReturnScreen(navController = navController)
        }

        composable(Route.AveragePrice) {
            AveragePriceScreen(navController = navController)
        }

        composable(Route.AverageStrategy) {
            AverageStrategyScreen2(navController)
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
        composable(Route.CreateStock) {
            CreateStockScreen(navController)
        }
        composable(Route.ProfitStrategy) {
            ProfitStrategyScreen(navController)
        }

        composable(Route.StockList) {
            MyStocksScreen(navController)
        }
    }
}

object Route {
    const val Home = "home"
    const val TradingReturn = "trading-return"
    const val AveragePrice = "average-price"
    const val AverageStrategy = "average-strategy"
    const val AutoRejection = "auto-rejection"
    const val ProfitPerTick = "profit-per-tick/{lots}/{price}"
    const val AllCalculator = "all-calculator"
    const val ProfitStrategy = "profit-strategy"
    const val CreateStock = "create-stock"
    const val StockList = "stock-list"

}
