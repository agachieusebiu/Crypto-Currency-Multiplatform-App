package org.example.project.coins.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import org.example.project.coins.presentation.component.PerformanceChart
import org.example.project.theme.LocalCoinRoutineColorsPalette
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the main screen for displaying a list of coins.
 * It observes the state from the CoinsListViewModel and updates the UI accordingly.
 *
 * @param onCoinClicked A lambda function that is called when a coin item is clicked, passing the coin ID as a parameter.
 */
@Composable
fun CoinsListScreen(
    onCoinClicked: (String) -> Unit
) {
    val coinsListViewModel = koinViewModel<CoinsListViewModel>()
    val state by coinsListViewModel.state.collectAsStateWithLifecycle()

    CoinsListContent(
        state = state,
        onDismissChart = { coinsListViewModel.onDismissChart() },
        onCoinLongPressed = { coinId -> coinsListViewModel.onCoinLongPressed(coinId) },
        onCoinClicked = onCoinClicked
    )
}

/**
 * Composable function that displays the content of the CoinsListScreen based on the provided state.
 * It shows a list of coins and, if a chart state is present, displays a dialog with the coin's price chart.
 *
 * @param state The current state of the coins list, including the list of coins and any chart state.
 * @param onDismissChart A lambda function that is called when the chart dialog is dismissed.
 * @param onCoinLongPressed A lambda function that is called when a coin item is long-pressed, passing the coin ID as a parameter.
 * @param onCoinClicked A lambda function that is called when a coin item is clicked, passing the coin ID as a parameter.
 */
@Composable
fun CoinsListContent(
    state: CoinsState,
    onDismissChart: () -> Unit,
    onCoinLongPressed: (String) -> Unit,
    onCoinClicked: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.chartState != null) {
            CoinChartDialog(
                uiChartState = state.chartState,
                onDismiss = { onDismissChart() }
            )
        }
        CoinsList(
            coins = state.coins,
            onCoinLongPressed = onCoinLongPressed,
            onCoinClicked = onCoinClicked
        )
    }
}

/**
 * Composable function that displays a list of coins in a LazyColumn.
 * Each coin item can be clicked or long-pressed to trigger corresponding actions.
 *
 * @param coins A list of UiCoinListItem representing the coins to be displayed.
 * @param onCoinLongPressed A lambda function that is called when a coin item is long-pressed, passing the coin ID as a parameter.
 * @param onCoinClicked A lambda function that is called when a coin item is clicked, passing the coin ID as a parameter.
 */
@Composable
fun CoinsList(
    coins: List<UiCoinListItem>,
    onCoinLongPressed: (String) -> Unit,
    onCoinClicked: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = WindowInsets.systemBars.asPaddingValues(),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "🔥 Top Coins:",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(coins) { coin ->
                CoinListItem(
                    coin = coin,
                    onCoinLongPressed = onCoinLongPressed,
                    onCoinClicked = onCoinClicked
                )
            }
        }
    }
}

/**
 * Composable function that represents a single item in the coins list.
 * It displays the coin's icon, name, symbol, price, and 24h change.
 * The item can be clicked to view details or long-pressed to view the price chart.
 *
 * @param coin The UiCoinListItem representing the coin to be displayed.
 * @param onCoinLongPressed A lambda function that is called when the coin item is long-pressed, passing the coin ID as a parameter.
 * @param onCoinClicked A lambda function that is called when the coin item is clicked, passing the coin ID as a parameter.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CoinListItem(
    coin: UiCoinListItem,
    onCoinLongPressed: (String) -> Unit,
    onCoinClicked: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onCoinClicked(coin.id) },
                onLongClick = { onCoinLongPressed(coin.id) }
            )
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
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = coin.symbol,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = coin.formattedPrice,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = coin.formattedChange,
                color = if (coin.isPositive) LocalCoinRoutineColorsPalette.current.profitGreen else LocalCoinRoutineColorsPalette.current.lossRed,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )
        }
    }
}

/**
 * Composable function that displays an AlertDialog showing the 24h price chart for a specific coin.
 * The dialog shows a loading indicator while the chart data is being loaded, and once loaded, it displays the performance chart.
 *
 * @param uiChartState The state of the chart, including the coin name, loading status, and sparkline data.
 * @param onDismiss A lambda function that is called when the dialog is dismissed.
 */
@Composable
fun CoinChartDialog(
    uiChartState: UiChartState,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "24h Price Chart for ${uiChartState.coinName}"
            )
        },
        text = {
            if (uiChartState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            } else {
                PerformanceChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    nodes = uiChartState.sparkLine,
                    profitColor = LocalCoinRoutineColorsPalette.current.profitGreen,
                    lossColor = LocalCoinRoutineColorsPalette.current.lossRed
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Close")
            }
        }
    )
}