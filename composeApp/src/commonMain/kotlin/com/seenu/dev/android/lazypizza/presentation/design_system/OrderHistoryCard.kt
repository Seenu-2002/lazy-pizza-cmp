package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seenu.dev.android.lazypizza.LocalCurrencyFormatter
import com.seenu.dev.android.lazypizza.presentation.state.OrderHistoryUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.OrderStatus
import com.seenu.dev.android.lazypizza.presentation.theme.InstrumentSans
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body4Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import com.seenu.dev.android.lazypizza.presentation.utils.getBackgroundColor
import com.seenu.dev.android.lazypizza.presentation.utils.getStringRes
import com.seenu.dev.android.lazypizza.presentation.utils.roundTo2Digits
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.count_and_price
import lazypizza.composeapp.generated.resources.total_amount
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class OrderHistoryDataProvider : PreviewParameterProvider<OrderHistoryUiModel> {
    override val values: Sequence<OrderHistoryUiModel>
        get() = sequenceOf(
            OrderHistoryUiModel(
                id = "Order #123",
                items = listOf(
                    OrderItemUiModel("Margherita Pizza", 2, 15.99),
                    OrderItemUiModel("Pepperoni Pizza", 1, 18.49)
                ),
                status = OrderStatus.COMPLETED,
                dateFormatted = "12 September 2025"
            ),
            OrderHistoryUiModel(
                id = "Order #124",
                items = listOf(
                    OrderItemUiModel("Veggie Pizza", 3, 12.75)
                ),
                status = OrderStatus.IN_PROGRESS,
                dateFormatted = "15 September 2025"
            )
        )

}

@Preview
@Composable
private fun OrderHistoryCardPreview(@PreviewParameter(provider = OrderHistoryDataProvider::class) data: OrderHistoryUiModel) {
    LazyPizzaTheme {
        OrderHistoryCard(modifier = Modifier.fillMaxWidth(), data = data)
    }
}

@Composable
fun OrderHistoryCard(modifier: Modifier = Modifier, data: OrderHistoryUiModel) {
    val currencyFormatter = LocalCurrencyFormatter.current
    Column(
        modifier = modifier
            .dropShadow(
                shape = MaterialTheme.shapes.medium,
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    radius = 16.dp,
                    spread = 0.dp,
                    color = Color(0x0A03131F),
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceHigher,
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = data.id,
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colorScheme.textPrimary
                )
                Text(
                    text = data.dateFormatted,
                    style = MaterialTheme.typography.body4Regular,
                    color = MaterialTheme.colorScheme.textSecondary

                )
            }
            Text(
                text = stringResource(data.status.getStringRes()),
                fontFamily = InstrumentSans,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = data.status.getBackgroundColor(),
                        shape = CircleShape
                    )
                    .padding(vertical = 0.dp, horizontal = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1F)
                    .align(Alignment.Bottom)
            ) {
                for (item in data.items) {
                    Text(
                        text = stringResource(Res.string.count_and_price, item.quantity, item.name),
                        style = MaterialTheme.typography.body4Regular,
                        color = MaterialTheme.colorScheme.textPrimary
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.Bottom), horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(Res.string.total_amount),
                    style = MaterialTheme.typography.body4Regular,
                    color = MaterialTheme.colorScheme.textSecondary
                )
                Text(
                    text = currencyFormatter.format(data.totalPrice.roundTo2Digits()),
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colorScheme.textPrimary
                )
            }
        }
    }
}