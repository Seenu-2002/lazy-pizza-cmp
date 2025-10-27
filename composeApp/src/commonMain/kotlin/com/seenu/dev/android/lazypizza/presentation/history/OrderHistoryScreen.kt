package com.seenu.dev.android.lazypizza.presentation.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaInfoCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaTextButton
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title1SemiBold
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.not_signed_in
import lazypizza.composeapp.generated.resources.not_signed_in_msg
import lazypizza.composeapp.generated.resources.order_history
import lazypizza.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun OrderHistoryScreenPreview() {
    LazyPizzaTheme {
        OrderHistoryScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(title = {
            Text(
                text = stringResource(Res.string.order_history),
                style = MaterialTheme.typography.body1Medium
            )
        })

        Spacer(modifier = Modifier.height(120.dp))
        LazyPizzaInfoCard(
            title = stringResource(Res.string.not_signed_in),
            message = stringResource(Res.string.not_signed_in_msg),
            actionLabel = stringResource(Res.string.sign_in),
            onActionClick = { /* TODO: Handle sign-in action */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
