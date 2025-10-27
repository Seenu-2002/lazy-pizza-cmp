package com.seenu.dev.android.lazypizza

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import coil3.compose.setSingletonImageLoaderFactory
import com.seenu.dev.android.lazypizza.presentation.navigation.LazyPizzaNavigationMobile
import com.seenu.dev.android.lazypizza.presentation.navigation.LazyPizzaNavigationTablet
import com.seenu.dev.android.lazypizza.presentation.navigation.NavItem
import com.seenu.dev.android.lazypizza.presentation.navigation.Route
import com.seenu.dev.android.lazypizza.presentation.navigation.getAsyncImageLoader
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

@Composable
fun LazyPizzaApp() {
    LazyPizzaTheme {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        val isTablet = isExpanded()
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val selected by derivedStateOf {
            val currentRoute = navBackStackEntry?.destination?.route?.toRouteOrNull()
            Logger.e {
                "Current Route: ${navBackStackEntry?.destination?.route}"
            }
            Logger.e {
                "Parsed Route: $currentRoute"
            }
            when (currentRoute) {
                NavItem.PizzaListItem.route -> NavItem.PizzaListItem
                NavItem.CartItem.route -> NavItem.CartItem
                NavItem.HistoryItem.route -> NavItem.HistoryItem
                else -> NavItem.PizzaListItem
            }
        }
        val items = remember {
            listOf(
                NavItem.PizzaListItem,
                NavItem.CartItem,
                NavItem.HistoryItem
            )
        }

        if (isTablet) {
            LazyPizzaNavigationTablet(
                navController = navController,
                navItems = items,
                selectedNavItem = selected,
                onItemSelected = {
                    navController.navigate(it.route) {
                        popUpTo(0)
                    }
                }
            )
        } else {
            LazyPizzaNavigationMobile(
                navController = navController,
                navItems = items,
                selectedNavItem = selected,
                onItemSelected = {
                    navController.navigate(it.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

fun String.toRouteOrNull(): Route? {
    return when (this) {
        Route.PizzaList::class.qualifiedName -> Route.PizzaList
        Route.Cart::class.qualifiedName -> Route.Cart
        Route.History::class.qualifiedName -> Route.History
        else -> {
            try {
                Json.decodeFromString<Route>(this)
            } catch (exp: SerializationException) {
                Logger.e {
                    "Error parsing route from string: $this, exception: $exp"
                }
                null
            }
        }
    }
}