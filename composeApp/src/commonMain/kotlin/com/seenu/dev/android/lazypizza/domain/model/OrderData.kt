package com.seenu.dev.android.lazypizza.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class OrderData @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val time: Instant,
    val comments: String
)