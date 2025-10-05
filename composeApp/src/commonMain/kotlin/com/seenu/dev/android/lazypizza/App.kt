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
import com.seenu.dev.android.lazypizza.presentation.navigation.Route
import com.seenu.dev.android.lazypizza.presentation.pizza_list.PizzaListScreen
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LazyPizzaTheme {
        val navController = rememberNavController()

        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = Route.PizzaList,
        ) {

            composable<Route.PizzaList> {
                PizzaListScreen()
            }

            composable<Route.PizzaDetail> {
                Text("Yet to be implemented")
            }
        }
    }
}