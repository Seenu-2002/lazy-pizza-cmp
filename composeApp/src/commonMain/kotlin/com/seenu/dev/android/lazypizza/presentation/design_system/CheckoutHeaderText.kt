package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.comments
import org.jetbrains.compose.resources.stringResource


@Composable
fun CheckoutHeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.label2Semibold,
        color = MaterialTheme.colorScheme.textSecondary
    )
}