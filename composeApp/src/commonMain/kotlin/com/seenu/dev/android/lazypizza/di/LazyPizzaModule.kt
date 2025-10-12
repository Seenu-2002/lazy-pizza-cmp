package com.seenu.dev.android.lazypizza.di

import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.LazyPizzaRepositoryImpl
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

    @Single(binds = [LazyPizzaRepository::class])
    fun lazyPizzaRepository(): LazyPizzaRepository = LazyPizzaRepositoryImpl()

}