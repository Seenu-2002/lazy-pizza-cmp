package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.cancel
import lazypizza.composeapp.generated.resources.ok
import lazypizza.composeapp.generated.resources.select_date
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun SelectDateDialog(
    modifier: Modifier = Modifier,
    onDateSelected: (Long) -> Unit,
    onCancel: () -> Unit,
) {

    val now = Clock.System.now()
    val today = now
        .toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).let {
            LocalDateTime(
                year = it.year,
                month = it.month,
                day = it.day,
                hour = 0,
                minute = 0,
            ).toInstant(timeZone = TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = now.toEpochMilliseconds(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= today
            }
        },
    )
    BasicAlertDialog(
        onDismissRequest = onCancel, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceHigher
                )
                .padding(vertical = 12.dp)
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth(),
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceHigher,
                    dividerColor = MaterialTheme.colorScheme.outline
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    onClick = {
                        onDateSelected(
                            datePickerState.selectedDateMillis ?: today
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