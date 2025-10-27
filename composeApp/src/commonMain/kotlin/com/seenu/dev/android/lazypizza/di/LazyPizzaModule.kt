package com.seenu.dev.android.lazypizza.di

import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.FirebaseLazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.InMemoryCartRepository
import com.seenu.dev.android.lazypizza.presentation.cart.PizzaCartViewModel
import com.seenu.dev.android.lazypizza.presentation.pizza_detail.PizzaDetailViewModel
import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class LazyPizzaModule {

    @KoinViewModel
    fun pizzaListViewModel(
        repository: LazyPizzaRepository,
        cartRepository: LazyPizzaCartRepository
    ): PizzaListViewModel =
        PizzaListViewModel(repository, cartRepository)

    @KoinViewModel
    fun pizzaDetailViewModel(repository: LazyPizzaRepository): PizzaDetailViewModel =
        PizzaDetailViewModel(repository)

    @KoinViewModel
    fun pizzaCartViewModel(repository: LazyPizzaCartRepository): PizzaCartViewModel =
        PizzaCartViewModel(repository)

    @Single(binds = [LazyPizzaRepository::class])
    fun lazyPizzaRepository(): LazyPizzaRepository = FirebaseLazyPizzaRepository()

    @Single(binds = [LazyPizzaCartRepository::class])
    fun lazyPizzaCartRepository(): LazyPizzaCartRepository = InMemoryCartRepository()

}