package com.seenu.dev.android.lazypizza.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.seenu.dev.android.lazypizza.presentation.cart.PizzaCartScreen
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaNavBarItem
import com.seenu.dev.android.lazypizza.presentation.history.OrderHistoryScreen
import com.seenu.dev.android.lazypizza.presentation.pizza_detail.PizzaDetailScreen
import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListScreen
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHigher
import com.seenu.dev.android.lazypizza.presentation.theme.title4
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.ic_cart
import lazypizza.composeapp.generated.resources.ic_history
import lazypizza.composeapp.generated.resources.ic_menu
import lazypizza.composeapp.generated.resources.nav_cart
import lazypizza.composeapp.generated.resources.nav_history
import lazypizza.composeapp.generated.resources.nav_menu
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LazyPizzaNavigationMobile(
    navController: NavHostController,
    navItems: List<NavItem>,
    selectedNavItem: NavItem,
    onItemSelected: (NavItem) -> Unit = {}
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
        bottomBar = {
            LazyPizzaBottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                navItems,
                selectedItem = selectedNavItem,
                onItemSelected = onItemSelected
            )
        }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyPizzaNavHost(
                navController = navController,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}

@Composable
fun LazyPizzaNavigationTablet(
    navController: NavHostController,
    navItems: List<NavItem>,
    selectedNavItem: NavItem,
    onItemSelected: (NavItem) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavigationRail { }
            LazyPizzaNavHost(
                navController = navController,
                modifier = Modifier.fillMaxHeight().weight(1F),
            )
        }
    }
}

@Composable
private fun LazyPizzaNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
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

        composable<Route.Cart> {
            PizzaCartScreen(onBackToMenu = {
                navController.navigate(Route.PizzaList) {
                    popUpTo(0)
                }
            })
        }

        composable<Route.History> {
            OrderHistoryScreen()
        }
    }
}

@Preview
@Composable
private fun LazyPizzaBottomBarPreview() {
    LazyPizzaTheme {
        LazyPizzaBottomBar(
            items = listOf(
                NavItem.PizzaListItem,
                NavItem.CartItem,
                NavItem.HistoryItem
            ),
            selectedItem = NavItem.PizzaListItem
        )
    }
}

sealed class NavItem {
    abstract val route: Route
    abstract val iconRes: DrawableResource
    abstract val titleRes: StringResource

    data object PizzaListItem : NavItem() {
        override val route: Route = Route.PizzaList
        override val iconRes: DrawableResource = Res.drawable.ic_menu
        override val titleRes: StringResource = Res.string.nav_menu
    }

    data object CartItem : NavItem() {
        override val route: Route = Route.Cart
        override val iconRes: DrawableResource = Res.drawable.ic_cart
        override val titleRes: StringResource = Res.string.nav_cart
    }

    data object HistoryItem : NavItem() {
        override val route: Route = Route.History
        override val iconRes: DrawableResource = Res.drawable.ic_history
        override val titleRes: StringResource = Res.string.nav_history
    }
}

@Composable
fun LazyPizzaBottomBar(
    modifier: Modifier = Modifier,
    items: List<NavItem>,
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit = {}
) {
    val shape = remember {
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )
    }
    NavigationBar(
        modifier = modifier
            .dropShadow(
                shape = shape,
                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                    radius = 16.dp,
                    spread = 0.dp,
                    color = Color(0x0F03131F),
                    offset = DpOffset(x = 0.dp, (-4).dp)
                )
            )
            .clip(shape),
        containerColor = MaterialTheme.colorScheme.surfaceHigher
    ) {
        for (item in items) {
            LazyPizzaNavBarItem(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight()
                    .weight(1F),
                selected = item == selectedItem,
                onClick = { onItemSelected(item) },
                icon = painterResource(item.iconRes),
                label = stringResource(item.titleRes),
            )
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