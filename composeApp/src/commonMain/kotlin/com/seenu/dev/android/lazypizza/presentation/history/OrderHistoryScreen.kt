package com.seenu.dev.android.lazypizza.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.lazypizza.LocalDimensions
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaInfoCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaTextButton
import com.seenu.dev.android.lazypizza.presentation.design_system.OrderHistoryCard
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.go_to_menu
import lazypizza.composeapp.generated.resources.no_orders
import lazypizza.composeapp.generated.resources.no_orders_msg
import lazypizza.composeapp.generated.resources.not_signed_in
import lazypizza.composeapp.generated.resources.not_signed_in_msg
import lazypizza.composeapp.generated.resources.order_history
import lazypizza.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun OrderHistoryScreenPreview() {
    LazyPizzaTheme {
        OrderHistoryScreen({}, {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(openLoginScreen: () -> Unit, goToMenu: () -> Unit) {

    val viewModel: OrderHistoryViewModel = koinViewModel()
    val orderHistoryUiState by viewModel.orderHistoryUiState.collectAsStateWithLifecycle()
    val dimensions = LocalDimensions.current.historyScreen

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(Res.string.order_history),
                    style = MaterialTheme.typography.body1Medium
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        if (!orderHistoryUiState.isUserLoggedIn) {
            Spacer(modifier = Modifier.height(120.dp))
            LazyPizzaInfoCard(
                title = stringResource(Res.string.not_signed_in),
                message = stringResource(Res.string.not_signed_in_msg),
                actionLabel = stringResource(Res.string.sign_in),
                onActionClick = openLoginScreen,
                modifier = Modifier.fillMaxWidth()
            )
        } else {

            if (orderHistoryUiState.orders.isEmpty()) {
                LazyPizzaInfoCard(
                    title = stringResource(Res.string.no_orders),
                    message = stringResource(Res.string.no_orders_msg),
                    actionLabel = stringResource(Res.string.go_to_menu),
                    onActionClick = goToMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp),
                    columns = GridCells.Fixed(dimensions.columnCount),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(orderHistoryUiState.orders.size) { index ->
                        val order = orderHistoryUiState.orders[index]
                        OrderHistoryCard(
                            modifier = Modifier
                                .fillMaxWidth(),
                            data = order
                        )
                    }
                }
            }
        }


    }
}
