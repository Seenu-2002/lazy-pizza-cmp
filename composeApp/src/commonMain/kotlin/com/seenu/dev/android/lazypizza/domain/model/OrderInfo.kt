package com.seenu.dev.android.lazypizza.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class OrderInfo @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val time: Instant
)