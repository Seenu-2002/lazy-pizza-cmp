package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Phone
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger.Companion.v
import com.seenu.dev.android.lazypizza.presentation.state.OtpState
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body2Regular
import com.seenu.dev.android.lazypizza.presentation.theme.body4Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.error_otp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun LoginNumberCardPreview() {
    LazyPizzaTheme {
        var text by remember { mutableStateOf("+919999999999") }
        var isInOtpValidationMode by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
//            while (true) {
//                kotlinx.coroutines.delay(2000)
//                isInOtpValidationMode = !isInOtpValidationMode
//            }
        }
        LoginNumberCard(
            modifier = Modifier.fillMaxWidth(),
            number = text,
            onValueChange = { text = it },
            otpState = OtpState(),
            isInOtpValidationMode = isInOtpValidationMode,
        )
    }
}

@Composable
fun LoginNumberCard(
    number: String,
    otpState: OtpState,
    modifier: Modifier = Modifier,
    isInOtpValidationMode: Boolean = false,
    onValueChange: (String) -> Unit,
    onOtpAction: (OtpAction) -> Unit = {},
) {
    Column(modifier = modifier.animateContentSize()) {
        BasicTextField(
            value = number,
            onValueChange = onValueChange,
            enabled = !isInOtpValidationMode,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                keyboardType = Phone
            ),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    contentPadding = PaddingValues(
                        vertical = 12.dp,
                        horizontal = 20.dp
                    ),
                    value = number,
                    innerTextField = innerTextField,
                    placeholder = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "+10000000000",
                            style = MaterialTheme.typography.body2Regular
                        )
                    },
                    singleLine = true,
                    enabled = !isInOtpValidationMode,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.textPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.textPrimary,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceHighest,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceHighest,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = CircleShape,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                )
            },
        )

        if (isInOtpValidationMode) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                for ((index, number) in otpState.code.withIndex()) {
                    OtpField(
                        num = number,
                        error = otpState.isValid == false,
                        focusRequester = otpState.focusRequesters[index],
                        onFocusChanged = { isFocused ->
                            if (isFocused) {
                                onOtpAction(OtpAction.OnFocused(index))
                            }
                        },
                        onNumberChanged = { newNumber ->
                            onOtpAction(OtpAction.OnEnterNumber(newNumber, index))
                        },
                        onKeyboardBack = {
                            onOtpAction(OtpAction.OnKeyboardBack(index))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                    )
                }
            }

            otpState.isValid?.let { isValid ->
                if (!isValid) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.error_otp),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.body4Regular
                    )
                }
            }
        }
    }
}

sealed interface OtpAction {
    data class OnEnterNumber constructor(val number: Int?, val index: Int) : OtpAction
    data class OnFocused constructor(val index: Int) : OtpAction
    data class OnKeyboardBack constructor(val index: Int) : OtpAction
}