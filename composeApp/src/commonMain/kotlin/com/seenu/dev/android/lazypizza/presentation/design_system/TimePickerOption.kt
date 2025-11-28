package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seenu.dev.android.lazypizza.presentation.checkout.PickupTime
import com.seenu.dev.android.lazypizza.presentation.theme.InstrumentSans
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Medium
import com.seenu.dev.android.lazypizza.presentation.theme.textPrimary
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.earliest_available
import lazypizza.composeapp.generated.resources.schedule_time
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimePickerOption(
    selected: PickupTime,
    onEarliestTimeSelected: () -> Unit,
    onScheduleTimeSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTablet = isExpanded()
    if (isTablet) {
        TimePickerOptionTablet(
            selected = selected,
            onEarliestTimeSelected = onEarliestTimeSelected,
            onScheduleTimeSelected = onScheduleTimeSelected,
            modifier = modifier
        )
    } else {
        TimePickerOptionMobile(
            selected = selected,
            onEarliestTimeSelected = onEarliestTimeSelected,
            onScheduleTimeSelected = onScheduleTimeSelected,
            modifier = modifier
        )
    }
}

@Composable
fun TimePickerOptionTablet(
    selected: PickupTime,
    onEarliestTimeSelected: () -> Unit,
    onScheduleTimeSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        TimePickerOptionItem(
            isSelected = selected is PickupTime.EarliestAvailableTime,
            text = stringResource(Res.string.earliest_available),
            onClick = onEarliestTimeSelected,
            modifier = Modifier.weight(1F)
        )
        Spacer(modifier = Modifier.width(12.dp))
        TimePickerOptionItem(
            isSelected = selected is PickupTime.ScheduledTime,
            text = stringResource(Res.string.schedule_time),
            onClick = onScheduleTimeSelected,
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
fun TimePickerOptionMobile(
    selected: PickupTime,
    onEarliestTimeSelected: () -> Unit,
    onScheduleTimeSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TimePickerOptionItem(
            isSelected = selected is PickupTime.EarliestAvailableTime,
            text = stringResource(Res.string.earliest_available),
            onClick = onEarliestTimeSelected,
            modifier = Modifier.fillMaxWidth()
        )
        TimePickerOptionItem(
            isSelected = selected is PickupTime.ScheduledTime,
            text = stringResource(Res.string.schedule_time),
            onClick = onScheduleTimeSelected,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun TimePickerOptionItemPreview() {
    LazyPizzaTheme {
        TimePickerOptionItem(
            isSelected = true,
            text = stringResource(Res.string.earliest_available),
            onClick = {}
        )
    }
}

@Composable
fun TimePickerOptionItem(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(shape = CircleShape, color = Color.Transparent)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val textColor = if (isSelected) {
            MaterialTheme.colorScheme.textPrimary
        } else {
            MaterialTheme.colorScheme.textSecondary
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.textSecondary,
                disabledUnselectedColor = MaterialTheme.colorScheme.textSecondary
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body3Medium,
            color = textColor,
        )
    }
}