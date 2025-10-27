package com.seenu.dev.android.lazypizza.presentation.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaInfoCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaTextButton
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.back_to_menu
import lazypizza.composeapp.generated.resources.cart
import lazypizza.composeapp.generated.resources.empty_cart
import lazypizza.composeapp.generated.resources.empty_cart_msg
import lazypizza.composeapp.generated.resources.not_signed_in
import lazypizza.composeapp.generated.resources.not_signed_in_msg
import lazypizza.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun PizzaCartScreenPreview() {
    LazyPizzaTheme { PizzaCartScreen({}) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaCartScreen(onBackToMenu: () -> Unit) {

    val viewModel: PizzaCartViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.events.collect {
            when (it) {
                CartSideEffect.Checkout -> {
                    // TODO: Checkout logic
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    stringResource(Res.string.cart),
                    style = MaterialTheme.typography.body1Medium,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            },
        )

        Spacer(modifier = Modifier.height(120.dp))
        EmptyCartCard(onBackToMenu = onBackToMenu, modifier = Modifier.fillMaxWidth())
    }

}

@Composable
fun EmptyCartCard(onBackToMenu: () -> Unit, modifier: Modifier = Modifier) {
    LazyPizzaInfoCard(
        title = stringResource(Res.string.empty_cart),
        message = stringResource(Res.string.empty_cart_msg),
        actionLabel = stringResource(Res.string.back_to_menu),
        onActionClick = onBackToMenu,
        modifier = modifier
    )
}