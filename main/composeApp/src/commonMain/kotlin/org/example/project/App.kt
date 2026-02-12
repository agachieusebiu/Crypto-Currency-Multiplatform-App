package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.coins.presentation.CoinsListScreen
import org.example.project.core.navigation.Buy
import org.example.project.core.navigation.Coins
import org.example.project.core.navigation.Portfolio
import org.example.project.core.navigation.Sell
import org.example.project.portofolio.presentation.PortfolioScreen
import org.example.project.theme.CoinRoutineTheme
import org.example.project.trade.presentation.buy.BuyScreen
import org.example.project.trade.presentation.sell.SellScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * The main composable function for the CoinRoutine application, responsible for setting up the navigation graph and defining the different screens of the app.
 *
 * This function initializes a NavHostController to manage navigation between screens and uses a NavHost to define the routes and corresponding composable screens for the Portfolio, Coins List, Buy, and Sell functionalities. Each screen is associated with a specific route, and navigation actions are defined to allow users to move between these screens seamlessly.
 */
@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()
    CoinRoutineTheme {
        NavHost(
            navController = navController,
            startDestination = Portfolio,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<Portfolio> {
                PortfolioScreen(
                    onCoinItemClicked = { coinId ->
                        navController.navigate(Sell(coinId))
                    },
                    onDiscoverCoinsClicked = {
                        navController.navigate(Coins)
                    }
                )
            }

            composable<Coins> {
                CoinsListScreen { coinId ->
                    navController.navigate(Buy(coinId))
                }
            }

            composable<Buy> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Buy>().coinId
                BuyScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }

            composable<Sell> { navBackStackEntry ->
                val coinId: String = navBackStackEntry.toRoute<Sell>().coinId
                SellScreen(
                    coinId = coinId,
                    navigateToPortfolio = {
                        navController.navigate(Portfolio) {
                            popUpTo(Portfolio) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}