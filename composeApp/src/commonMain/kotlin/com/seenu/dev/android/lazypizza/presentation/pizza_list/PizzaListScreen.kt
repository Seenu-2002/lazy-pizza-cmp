package com.seenu.dev.android.lazypizza.presentation.pizza_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.lazypizza.di.LazyPizzaModule
import com.seenu.dev.android.lazypizza.presentation.design_system.FoodFilterChip
import com.seenu.dev.android.lazypizza.presentation.design_system.FoodPreviewItemCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaSearchBar
import com.seenu.dev.android.lazypizza.presentation.state.FoodSection
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Regular
import com.seenu.dev.android.lazypizza.presentation.theme.body3Bold
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.app_name
import lazypizza.composeapp.generated.resources.ic_phone
import lazypizza.composeapp.generated.resources.ic_pizza
import lazypizza.composeapp.generated.resources.img_banner
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.ksp.generated.module

@Preview
@Composable
private fun PizzaListScreenPreview() {
    KoinApplication(application = {
        modules(LazyPizzaModule().module)
    }) {
        LazyPizzaTheme {
            PizzaListScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizzaListScreen(
    openDialer: () -> Unit = {},
) {
    val viewModel: PizzaListViewModel = koinViewModel()
    val itemsState by viewModel.items.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(end = 16.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(Res.drawable.ic_pizza),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(Res.string.app_name),
                            style = MaterialTheme.typography.body3Bold
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_phone),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+1 (555) 321-7890", style = MaterialTheme.typography.body1Regular)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.img_banner),
                    contentDescription = "Pizza Banner Image",
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(2.5F)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.height(8.dp))
                LazyPizzaSearchBar(
                    text = "",
                    onTextChange = {

                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
                FoodFilterChip(
                    filters = listOf("Pizza", "Drink", "Sauces", "Ice Cream"),
                    selectedFilter = null,
                    onFilterSelected = { filter ->

                    }
                )

                Box(
                    modifier = Modifier.fillMaxWidth().weight(1F),
                    contentAlignment = Alignment.Center
                ) {
                    when (val state = itemsState) {
                        is UiState.Loading, is UiState.Empty -> {
                            CircularProgressIndicator()
                        }

                        is UiState.Success -> {
                            FoodListContent(
                                data = state.data,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        is UiState.Error -> {
                            Text("Error: ${state.message}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodListContent(data: List<FoodSection>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(1)) {
        for (section in data) {
            item(key = section.type) {
                Text(
                    text = section.type.name,
                    style = MaterialTheme.typography.label2Semibold,
                    color = MaterialTheme.colorScheme.textSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            items(
                count = section.items.size,
                key = { index -> section.items[index].id }
            ) { index ->
                val item = section.items[index]
                FoodPreviewItemCard(
                    data = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                )
            }
        }
    }
}
