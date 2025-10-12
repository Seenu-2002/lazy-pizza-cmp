package com.seenu.dev.android.lazypizza

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.seenu.dev.android.lazypizza.core.formatter.CurrencyFormatter
import com.seenu.dev.android.lazypizza.presentation.navigation.Route
import com.seenu.dev.android.lazypizza.presentation.pizza_detail.PizzaDetailScreen
import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListScreen
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LazyPizzaTheme {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        val navController = rememberNavController()

        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = Route.PizzaList,
        ) {

            composable<Route.PizzaList> {
                PizzaListScreen(openDialer = {}, openDetailScreen = { id ->
                    navController.navigate(Route.PizzaDetail(id))
                })
            }

            composable<Route.PizzaDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.PizzaDetail>()
                PizzaDetailScreen(id = route.id, onBack = {
                    navController.navigateUp()
                })
            }
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(context, 0.3)
            .strongReferencesEnabled(true)
            .build()
    }
    .logger(DebugLogger())
    .crossfade(true)
    .build()