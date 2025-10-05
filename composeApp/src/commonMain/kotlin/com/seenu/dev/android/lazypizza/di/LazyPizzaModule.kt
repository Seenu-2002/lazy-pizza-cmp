package com.seenu.dev.android.lazypizza.di

import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListViewModel
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
class LazyPizzaModule {

    @KoinViewModel
    fun pizzaListViewModel(): PizzaListViewModel = PizzaListViewModel()

}