package com.seenu.dev.android.lazypizza.presentation.state

sealed interface UiState<T> {
    class Empty<T> constructor() : UiState<T>
    class Loading<T> constructor(): UiState<T>
    data class Success<T> constructor(val data: T) : UiState<T>
    data class Error<T> constructor(val message: String, val exception: Throwable? = null) : UiState<T>
}