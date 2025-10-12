package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.ic_clear
import lazypizza.composeapp.generated.resources.ic_search
import lazypizza.composeapp.generated.resources.search_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LazyPizzaSearchBarPreview() {
    LazyPizzaTheme {
        var text by remember { mutableStateOf("") }
        LazyPizzaSearchBar(
            text = text,
            onTextChange = {
                text = it
            }
        )
    }
}

// TODO: Add dropShadow to the search bar
@Composable
fun LazyPizzaSearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onClose: () -> Unit = {}
) {
    var hasFocus by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceHigher,
                shape = CircleShape
            )
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = 10.dp,
                    spread = 6.dp,
                    color = Color(0x03131F0A),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_search),
            contentDescription = "Search food",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        BasicTextField(
            modifier = Modifier
                .weight(1F)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    hasFocus = it.isFocused
                },
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            maxLines = 1,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.search_placeholder),
                            style = MaterialTheme.typography.body1Regular.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    innerTextField()
                }
            }
        )
//        if (text.isNotEmpty() || hasFocus) {
//            Spacer(modifier = Modifier.size(8.dp))
//            IconButton(onClick = onClose) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_clear),
//                    contentDescription = "Clear Search",
//                    tint = MaterialTheme.colorScheme.textPrimary,
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//        }
    }
}