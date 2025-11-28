package com.seenu.dev.android.lazypizza.presentation.mappers

import com.seenu.dev.android.lazypizza.domain.model.OrderInfo
import com.seenu.dev.android.lazypizza.presentation.state.OrderConfirmationUiModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(FormatStringsInDatetimeFormats::class)
private val dateFormatter = LocalDateTime.Format {
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    day()
    char(',')
    char(' ')
    hour()
    char(':')
    minute()
}

@OptIn(ExperimentalTime::class)
fun OrderInfo.toUiModel() = OrderConfirmationUiModel(
    id = id,
    pickupTimeLabel = dateFormatter.format(
        time.toLocalDateTime(
            timeZone = TimeZone.currentSystemDefault()
        )
    )
)