package com.seenu.dev.android.lazypizza.di

import com.seenu.dev.android.lazypizza.LazyPizzaDatabase
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.FirebaseLazyPizzaRepository
import com.seenu.dev.android.lazypizza.domain.repository.LocalCartRepository
import com.seenu.dev.android.lazypizza.presentation.cart.PizzaCartViewModel
import com.seenu.dev.android.lazypizza.LazyPizzaAppViewModel
import com.seenu.dev.android.lazypizza.presentation.pizza_detail.PizzaDetailViewModel
import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//@Module
//@ComponentScan
//class LazyPizzaModule {
//
//    @KoinViewModel
//    fun providesPizzaListViewModel(
//        repository: LazyPizzaRepository,
//        cartRepository: LazyPizzaCartRepository
//    ): PizzaListViewModel =
//        PizzaListViewModel(repository, cartRepository)
//
//    @KoinViewModel
//    fun providesPizzaDetailViewModel(repository: LazyPizzaRepository, cartRepository: LazyPizzaCartRepository): PizzaDetailViewModel =
//        PizzaDetailViewModel(repository, cartRepository)
//
//    @KoinViewModel
//    fun providesPizzaCartViewModel(repository: LazyPizzaCartRepository): PizzaCartViewModel =
//        PizzaCartViewModel(repository)
//
//    @Single(binds = [LazyPizzaRepository::class])
//    fun providesLazyPizzaRepository(): LazyPizzaRepository = FirebaseLazyPizzaRepository()
//
//    @Single(binds = [LazyPizzaCartRepository::class])
////    fun providesLazyPizzaCartRepository(): LazyPizzaCartRepository = InMemoryCartRepository()
//    fun providesLazyPizzaCartRepository(
//        database: LazyPizzaDatabase
//    ): LazyPizzaCartRepository = LocalCartRepository(database)
//
//    @Single
//    fun providesLazyPizzaDatabase(driver: SqlDriver): LazyPizzaDatabase {
//        return LazyPizzaDatabase(driver)
//    }
//
//}

// Create a Koin module using the DSL style
val lazyPizzaModule = module {
    viewModel { PizzaListViewModel(get(), get()) }
    viewModel { PizzaDetailViewModel(get(), get()) }
    viewModel { PizzaCartViewModel(get(), get()) }
    viewModel { LazyPizzaAppViewModel(get()) }
    single<LazyPizzaDatabase> { LazyPizzaDatabase(get()) }
    single<LazyPizzaRepository> { FirebaseLazyPizzaRepository() }
    single<LazyPizzaCartRepository> { LocalCartRepository(get()) }
}