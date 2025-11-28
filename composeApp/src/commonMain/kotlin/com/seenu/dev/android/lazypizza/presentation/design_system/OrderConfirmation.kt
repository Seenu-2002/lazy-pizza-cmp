package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.state.OrderConfirmationUiModel
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.label2Medium
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.back_to_menu
import lazypizza.composeapp.generated.resources.order_number
import lazypizza.composeapp.generated.resources.order_placed_msg
import lazypizza.composeapp.generated.resources.order_placed_title
import lazypizza.composeapp.generated.resources.pickup_time_with_colon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun OrderConfirmationPreview() {
    LazyPizzaTheme {
        OrderConfirmation(
            data = OrderConfirmationUiModel(
                id = "1212",
                pickupTimeLabel = "September 30, 06:30"
            ),
            goToMenu = {},
            modifier = Modifier
        )
    }
}

@Composable
fun OrderConfirmation(data: OrderConfirmationUiModel, goToMenu: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.order_placed_title),
            style = MaterialTheme.typography.title1Medium,
            color = MaterialTheme.colorScheme.textPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.order_placed_msg),
            style = MaterialTheme.typography.body3Regular,
            color = MaterialTheme.colorScheme.textSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.order_number).uppercase(),
                    style = MaterialTheme.typography.label2Medium,
                    color = MaterialTheme.colorScheme.textSecondary
                )
                Text(
                    text = "#${data.id}",
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.pickup_time_with_colon).uppercase(),
                    style = MaterialTheme.typography.label2Medium,
                    color = MaterialTheme.colorScheme.textSecondary
                )
                Text(
                    text = data.pickupTimeLabel,
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(Res.string.back_to_menu),
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable(onClick = goToMenu)
                .padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}