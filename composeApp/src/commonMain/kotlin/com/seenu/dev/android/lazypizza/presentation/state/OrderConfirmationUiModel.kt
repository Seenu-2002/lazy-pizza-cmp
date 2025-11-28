package com.seenu.dev.android.lazypizza.presentation.state

import kotlinx.datetime.LocalDateTime


data class OrderConfirmationUiModel constructor(
    val id: String,
    val pickupTimeLabel: String,
)