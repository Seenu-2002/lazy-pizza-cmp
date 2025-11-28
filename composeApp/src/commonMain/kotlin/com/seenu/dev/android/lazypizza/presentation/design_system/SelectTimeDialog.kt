package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.seenu.dev.android.lazypizza.presentation.theme.InstrumentSans
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import com.seenu.dev.android.lazypizza.presentation.theme.title4
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.cancel
import lazypizza.composeapp.generated.resources.hour
import lazypizza.composeapp.generated.resources.minute
import lazypizza.composeapp.generated.resources.ok
import lazypizza.composeapp.generated.resources.pickup_available
import lazypizza.composeapp.generated.resources.pickup_time
import lazypizza.composeapp.generated.resources.select_time
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeDialog(
    modifier: Modifier = Modifier,
    onTimeSelected: (Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    var hour by remember {
        mutableStateOf("")
    }
    var minute by remember {
        mutableStateOf("")
    }

    val isError = remember(hour, minute) {
        val hourInt = hour.toIntOrNull()
        val minuteInt = minute.toIntOrNull()

        if (hourInt == null || minuteInt == null) {
           return@remember false
        }

        // FIXME: Previous time on today issue
        when (hourInt) {
            10 -> {
                minuteInt < 15
            }
            21 -> {
                minuteInt > 45
            }
            !in 10..21 -> {
                true
            }
            else -> {
                false
            }
        }

    }
    val isValidTime = remember(hour, minute) {
        val hourInt = hour.toIntOrNull()
        val minuteInt = minute.toIntOrNull()

        if (hourInt == null || minuteInt == null) {
            false
        } else {
            hourInt in 0..23 && minuteInt in 0..59 && !isError
        }
    }

    val hourFocusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        hourFocusRequester.requestFocus()
    }

    BasicAlertDialog(
        onDismissRequest = onCancel, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceHigher
                )
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(Res.string.select_time),
                style = MaterialTheme.typography.label2Semibold,
                color = MaterialTheme.colorScheme.textSecondary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectTimeInput(
                    value = hour,
                    focusRequester = hourFocusRequester,
                    onValueChange = {
                        if (it.isEmpty()) {
                            hour = it
                            return@SelectTimeInput
                        }

                        val int = it.toIntOrNull()
                        if (int != null && int in 0..23) {
                            hour = it
                        }
                    },
                    label = stringResource(Res.string.hour),
                    modifier = Modifier.weight(1F)
                )
                Text(
                    text = " : ",
                    fontFamily = InstrumentSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 44.sp,
                    color = MaterialTheme.colorScheme.textPrimary,
                    modifier = Modifier.offset(y = -(8).dp)
                )
                SelectTimeInput(
                    value = minute,
                    onValueChange = {
                        if (it.isEmpty()) {
                            minute = it
                            return@SelectTimeInput
                        }

                        val int = it.toIntOrNull()
                        if (int != null && int in 0..59) {
                            minute = it
                        }
                    },
                    label = stringResource(Res.string.minute),
                    modifier = Modifier.weight(1F)
                )
            }
            AnimatedVisibility(
                visible = isError, modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.pickup_available, "10:15", "21:45"),
                    style = MaterialTheme.typography.title4,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = stringResource(Res.string.cancel),
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            onCancel()
                        }.clip(
                            shape = CircleShape
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                LazyPizzaTextButton(
                    enabled = isValidTime,
                    onClick = {
                        onTimeSelected(
                            hour.toIntOrNull() ?: 0,
                            minute.toIntOrNull() ?: 0
                        )
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
fun SelectTimeInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    label: String,
    modifier: Modifier = Modifier
) {
    var hasFocus by remember {
        mutableStateOf(false)
    }
    val borderColor = MaterialTheme.colorScheme.primary
    val background = MaterialTheme.colorScheme.surfaceHighest
    val shape = MaterialTheme.shapes.medium

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .let {
                    if (hasFocus) {
                        it.border(
                            width = 1.dp,
                            shape = shape,
                            color = borderColor
                        )
                    } else {
                        it.background(
                            color = background,
                            shape = shape
                        )
                    }
                }
                .padding(horizontal = 26.dp, vertical = 12.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    hasFocus = it.isFocused
                },
            textStyle = TextStyle(
                fontFamily = InstrumentSans,
                fontSize = 44.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.textPrimary,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (value.isEmpty()) {
                        Text(
                            text = "00",
                            fontFamily = InstrumentSans,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.textSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.title4,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}