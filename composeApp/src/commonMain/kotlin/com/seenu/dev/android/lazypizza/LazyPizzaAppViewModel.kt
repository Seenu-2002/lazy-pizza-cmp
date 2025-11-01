package com.seenu.dev.android.lazypizza

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LazyPizzaAppViewModel constructor(
    private val cartRepository: LazyPizzaCartRepository
) : ViewModel() {

    private val _cartCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val cartCount: StateFlow<Int> = _cartCount
        .onStart {
            fetchCartItemCount()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    private fun fetchCartItemCount() {
        viewModelScope.launch {
            cartRepository.getCartItemsCountFlow().collect {
                _cartCount.emit(it)
            }
        }
    }

}