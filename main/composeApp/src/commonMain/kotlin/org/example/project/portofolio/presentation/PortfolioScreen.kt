package org.example.project.portofolio.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.example.project.theme.LocalCoinRoutineColorsPalette
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Portfolio screen of the application.
 * This screen displays the user's portfolio balance and a list of owned coins.
 * It also handles loading states and provides navigation callbacks for coin item clicks and discovering new coins.
 *
 * @param onCoinItemClicked A callback function that is invoked when a coin item is clicked, passing the coin's ID as a parameter.
 * @param onDiscoverCoinsClicked A callback function that is invoked when the "Discover coins" button is clicked.
 */
@Composable
fun PortfolioScreen(
    onCoinItemClicked: (String) -> Unit,
    onDiscoverCoinsClicked: () -> Unit,
) {
    val portfolioViewModel = koinViewModel<PortfolioViewModel>()
    val state by portfolioViewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = LocalCoinRoutineColorsPalette.current.profitGreen,
                modifier = Modifier.size(32.dp)
            )
        }
    } else {
        PortfolioContent(
            state = state,
            onCoinItemClicked = onCoinItemClicked,
            onDiscoverCoinsClicked = onDiscoverCoinsClicked
        )
    }
}

/**
 * Composable function that displays the content of the Portfolio screen when the data is loaded.
 * It consists of a balance section at the top and a list of owned coins below it.
 *
 * @param state The current state of the portfolio, containing information about the portfolio value, cash balance, owned coins, and loading status.
 * @param onCoinItemClicked A callback function that is invoked when a coin item is clicked, passing the coin's ID as a parameter.
 * @param onDiscoverCoinsClicked A callback function that is invoked when the "Discover coins" button is clicked.
 */
@Composable
fun PortfolioContent(
    state: PortfolioState,
    onCoinItemClicked: (String) -> Unit,
    onDiscoverCoinsClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        PortfolioBalanceSection(
            portfolioValue = state.portfolioValue,
            cashBalance = state.cashBalance,
            showBuyButton = state.showBuyButton,
            onBuyButtonClicked = onDiscoverCoinsClicked
        )
        PortfolioCoinsList(
            coins = state.coins,
            onCoinItemClicked = onCoinItemClicked,
            onDiscoverCoinsClicked = onDiscoverCoinsClicked
        )
    }
}

/**
 * Composable function that displays the portfolio balance section at the top of the Portfolio screen.
 * It shows the total portfolio value, cash balance, and a "Buy Coin" button if there are coins in the portfolio.
 *
 * @param portfolioValue The total value of the user's portfolio formatted as a string.
 * @param cashBalance The cash balance available in the user's portfolio formatted as a string.
 * @param showBuyButton A boolean indicating whether to show the "Buy Coin" button (true if there are coins in the portfolio).
 * @param onBuyButtonClicked A callback function that is invoked when the "Buy Coin" button is clicked.
 */
@Composable
private fun PortfolioBalanceSection(
    portfolioValue: String,
    cashBalance: String,
    showBuyButton: Boolean,
    onBuyButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(32.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Total Value:",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = portfolioValue,
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )

            Row {
                Text(
                    text = "Cash Balance: ",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = cashBalance,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }

            if (showBuyButton) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onBuyButtonClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalCoinRoutineColorsPalette.current.profitGreen
                    ),
                    contentPadding = PaddingValues(horizontal = 64.dp)
                ) {
                    Text(
                        text = "buy Coin",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * Composable function that displays the list of owned coins in the user's portfolio.
 * If there are no coins, it shows an empty state with a message and a button to discover new coins.
 *
 * @param coins A list of UiPortfolioCoinItem representing the coins owned by the user.
 * @param onCoinItemClicked A callback function that is invoked when a coin item is clicked, passing the coin's ID as a parameter.
 * @param onDiscoverCoinsClicked A callback function that is invoked when the "Discover coins" button is clicked.
 */
@Composable
private fun PortfolioCoinsList(
    coins: List<UiPortfolioCoinItem>,
    onCoinItemClicked: (String) -> Unit,
    onDiscoverCoinsClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (coins.isEmpty()) {
            PortfolioEmptySection(
                onDiscoverCoinsClicked = onDiscoverCoinsClicked
            )
            return@Box
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "💰Owned Coins: ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(coins) { coin ->
                        CoinListItem(
                            coin = coin,
                            onCoinItemClicked = onCoinItemClicked
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable function that represents a single item in the list of owned coins.
 * It displays the coin's icon, name, amount in units, amount in fiat, and performance percentage.
 * The item is clickable and triggers a callback when clicked, passing the coin's ID.
 *
 * @param coin A UiPortfolioCoinItem representing the details of the coin to be displayed.
 * @param onCoinItemClicked A callback function that is invoked when the coin item is clicked, passing the coin's ID as a parameter.
 */
@Composable
private fun CoinListItem(
    coin: UiPortfolioCoinItem,
    onCoinItemClicked: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCoinItemClicked.invoke(coin.id)
            }
            .padding(16.dp)
    ) {
        AsyncImage(
            model = coin.iconUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(4.dp).clip(CircleShape).size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coin.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = coin.amountInUnitText,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = coin.amountInFIatText,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = coin.performancePercentText,
                color = if (coin.isPositive) LocalCoinRoutineColorsPalette.current.profitGreen else LocalCoinRoutineColorsPalette.current.lossRed,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
            )
        }
    }
}

/**
 * Composable function that displays an empty state when there are no coins in the user's portfolio.
 * It shows a message indicating that there are no coins and a button to discover new coins.
 *
 * @param onDiscoverCoinsClicked A callback function that is invoked when the "Discover coins" button is clicked.
 */
@Composable
private fun PortfolioEmptySection(
    onDiscoverCoinsClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = "No coins in your portfolio yet.",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )

        Button(
            onClick = onDiscoverCoinsClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalCoinRoutineColorsPalette.current.profitGreen
            ),
            contentPadding = PaddingValues(horizontal = 64.dp)
        ) {
            Text(
                text = "Discover coins.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}