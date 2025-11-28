package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.label1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.label1Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import com.seenu.dev.android.lazypizza.presentation.utils.roundTo2Digits
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.order_total
import lazypizza.composeapp.generated.resources.place_order
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun OrderTotalPreview() {
    LazyPizzaTheme {
        OrderTotal(
            total = 25.0,
            onPlaceOrder = {}
        )
    }
}

@Composable
fun OrderTotal(
    total: Double,
    onPlaceOrder: () -> Unit,
    enablePlaceOrder: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isTablet = isExpanded()
    if (isTablet) {
        OrderTotalTablet(
            total = total,
            onPlaceOrder = onPlaceOrder,
            enablePlaceOrder = enablePlaceOrder,
            modifier = modifier
        )
    } else {
        OrderTotalMobile(
            total = total,
            onPlaceOrder = onPlaceOrder,
            enablePlaceOrder = enablePlaceOrder,
            modifier = modifier
        )
    }
}

@Composable
fun OrderTotalMobile(
    total: Double,
    onPlaceOrder: () -> Unit,
    enablePlaceOrder: Boolean = true,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = LocalCurrencyFormatter.current
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(32.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surfaceHigher,
                            MaterialTheme.colorScheme.surfaceHigher,
                        )
                    )
                )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.order_total).uppercase(),
                style = MaterialTheme.typography.label1Medium,
                color = MaterialTheme.colorScheme.textSecondary
            )

            Text(
                text = currencyFormatter.format(total.roundTo2Digits()),
                style = MaterialTheme.typography.label1Semibold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyPizzaTextButton(
            onClick = onPlaceOrder,
            enabled = enablePlaceOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.place_order),
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun OrderTotalTablet(
    total: Double,
    onPlaceOrder: () -> Unit,
    enablePlaceOrder: Boolean = true,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = LocalCurrencyFormatter.current
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier.fillMaxWidth()
                .height(32.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Unspecified,
                            MaterialTheme.colorScheme.surfaceHigher,
                        )
                    )
                )
        )
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.order_total).uppercase(),
                style = MaterialTheme.typography.label1Medium,
                color = MaterialTheme.colorScheme.textSecondary
            )

            Text(
                text = currencyFormatter.format(total.roundTo2Digits()),
                style = MaterialTheme.typography.label1Semibold
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        LazyPizzaTextButton(onClick = onPlaceOrder, enabled = enablePlaceOrder) {
            Text(
                text = stringResource(Res.string.place_order),
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }
    }
}