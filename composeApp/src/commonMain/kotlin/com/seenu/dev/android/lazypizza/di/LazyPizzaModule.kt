package com.seenu.dev.android.lazypizza.di

import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.FirebaseLazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.InMemoryLazyPizzaRepository
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
    fun pizzaListViewModel(repository: LazyPizzaRepository): PizzaListViewModel =
        PizzaListViewModel(repository)

    @KoinViewModel
    fun pizzaDetailViewModel(repository: LazyPizzaRepository): PizzaDetailViewModel =
        PizzaDetailViewModel(repository)

    @Single(binds = [LazyPizzaRepository::class])
    fun lazyPizzaRepository(): LazyPizzaRepository = FirebaseLazyPizzaRepository()

}