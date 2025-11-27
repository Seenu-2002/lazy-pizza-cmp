package com.seenu.dev.android.lazypizza

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import coil3.compose.setSingletonImageLoaderFactory
import com.seenu.dev.android.lazypizza.firebase.LazyPizzaAuth
import com.seenu.dev.android.lazypizza.presentation.navigation.LazyPizzaNavigationMobile
import com.seenu.dev.android.lazypizza.presentation.navigation.LazyPizzaNavigationTablet
import com.seenu.dev.android.lazypizza.presentation.navigation.NavItem
import com.seenu.dev.android.lazypizza.presentation.navigation.Route
import com.seenu.dev.android.lazypizza.presentation.navigation.getAsyncImageLoader
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.utils.isExpanded
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LazyPizzaApp(
    auth: LazyPizzaAuth
) {
    LazyPizzaTheme {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        val isTablet = isExpanded()
        val navController = rememberNavController()
        val viewModel: LazyPizzaAppViewModel = koinViewModel()

        val cartItemCount by viewModel.cartCount.collectAsStateWithLifecycle()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.toRouteOrNull()
        val cartItemNavItem by derivedStateOf {
            NavItem.CartItem(cartItemCount)
        }
        val selected by derivedStateOf {
            when (currentRoute) {
                NavItem.PizzaListItem.route -> NavItem.PizzaListItem
                cartItemNavItem.route -> cartItemNavItem
                NavItem.HistoryItem.route -> NavItem.HistoryItem
                else -> NavItem.PizzaListItem
            }
        }
        val showNavBar by derivedStateOf {
            currentRoute in listOf(
                Route.PizzaList,
                Route.Cart,
                Route.History
            )
        }
        val items by derivedStateOf {
            listOf(
                NavItem.PizzaListItem,
                cartItemNavItem,
                NavItem.HistoryItem
            )
        }

        if (isTablet) {
            LazyPizzaNavigationTablet(
                auth = auth,
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
                auth = auth,
                showBottomBar = showNavBar,
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

fun NavBackStackEntry.toRouteOrNull(): Route? {
    return when (val route = destination.route) {
        Route.PizzaList::class.qualifiedName -> Route.PizzaList
        Route.Cart::class.qualifiedName -> Route.Cart
        Route.History::class.qualifiedName -> Route.History
        else -> {
            if (route?.startsWith(Route.PizzaDetail::class.qualifiedName ?: "") == true) {
                toRoute<Route.PizzaDetail>()
            } else {
                null
            }
        }
    }
}