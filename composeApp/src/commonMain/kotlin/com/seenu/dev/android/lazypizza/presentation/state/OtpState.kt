package com.seenu.dev.android.lazypizza.presentation.state

import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.focus.FocusRequester

data class OtpState constructor(
    val isLoading: Boolean = false,
    val code: MutableList<Int?> = (1..6).map { null }.toMutableStateList(),
    val focusRequesters: List<FocusRequester> = List(6) { FocusRequester() },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null
)