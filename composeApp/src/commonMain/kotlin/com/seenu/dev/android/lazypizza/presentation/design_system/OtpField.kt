package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body2Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun OtpFieldPreview() {
    LazyPizzaTheme {
        var num by remember { mutableStateOf<Int?>(null) }
        OtpField(
            num = num,
            focusRequester = remember { FocusRequester() },
            onFocusChanged = {},
            onNumberChanged = {
                num = it
            },
            onKeyboardBack = {},
            modifier = Modifier.size(width = 80.dp, height = 60.dp)
        )
    }
}


@Composable
fun OtpField(
    num: Int?,
    focusRequester: FocusRequester,
    error: Boolean = false,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {

    val text by remember(num) {
        val numAsString = num?.toString().orEmpty()
        mutableStateOf(
            TextFieldValue(
                text = numAsString,
                selection = TextRange(
                    if (num != null) {
                        numAsString.length
                    } else {
                        0
                    }
                )
            )
        )
    }
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceHighest, shape = CircleShape)
            .let {
                if (error) {
                    it.border(1.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                } else it
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                val inputText = it.text
                if (inputText.isEmpty()) {
                    onNumberChanged(null)
                } else if (inputText.length <= 1 && inputText.all(Char::isDigit)) {
                    onNumberChanged(inputText.toIntOrNull())
                }
            },
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onKeyEvent { event ->
                    val didPressDelete = event.key == Key.Backspace
                    if (didPressDelete) {
                        onKeyboardBack()
                    }
                    false
                }
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(isFocused)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            decorationBox = { innerTextField ->
                innerTextField()
                if (!isFocused && num == null) {
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.body2Regular,
                        color = MaterialTheme.colorScheme.textSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        textAlign = TextAlign.Center
                    )
                }
            },
            maxLines = 1,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            textStyle = MaterialTheme.typography.body2Regular.copy(
                textAlign = TextAlign.Center
            ),
        )
    }
}