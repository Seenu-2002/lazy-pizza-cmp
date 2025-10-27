package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.not_signed_in
import lazypizza.composeapp.generated.resources.not_signed_in_msg
import lazypizza.composeapp.generated.resources.sign_in
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun LazyPizzaInfoCardPreview() {
    LazyPizzaTheme {
        LazyPizzaInfoCard(
            title = stringResource(Res.string.not_signed_in),
            message = stringResource(Res.string.not_signed_in_msg),
            actionLabel = stringResource(Res.string.sign_in),
            onActionClick = { /* TODO: Handle sign-in action */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LazyPizzaInfoCard(
    title: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.title1Medium,
            color = MaterialTheme.colorScheme.textPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.body3Regular,
            color = MaterialTheme.colorScheme.textSecondary
        )
        Spacer(modifier = Modifier.height(24.dp))
        LazyPizzaTextButton(
            onClick = onActionClick,
        ) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}