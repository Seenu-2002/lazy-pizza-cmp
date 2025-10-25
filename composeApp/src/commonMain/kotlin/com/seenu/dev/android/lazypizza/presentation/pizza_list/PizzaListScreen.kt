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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.lazypizza.LocalDimensions
import com.seenu.dev.android.lazypizza.di.LazyPizzaModule
import com.seenu.dev.android.lazypizza.domain.model.FoodType
import com.seenu.dev.android.lazypizza.presentation.design_system.FoodFilterChip
import com.seenu.dev.android.lazypizza.presentation.design_system.FoodPreviewItemCard
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaSearchBar
import com.seenu.dev.android.lazypizza.presentation.state.FoodItemUiModel
import com.seenu.dev.android.lazypizza.presentation.state.FoodSection
import com.seenu.dev.android.lazypizza.presentation.state.UiState
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body1Regular
import com.seenu.dev.android.lazypizza.presentation.theme.body3Bold
import com.seenu.dev.android.lazypizza.presentation.theme.label2Semibold
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.utils.getStringRes
import kotlinx.coroutines.launch
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
    openDetailScreen: (String) -> Unit = {},
    openDialer: () -> Unit = {},
) {
    val viewModel: PizzaListViewModel = koinViewModel()
    val itemsState by viewModel.filteredItems.collectAsStateWithLifecycle()

    val dimensions = LocalDimensions.current.listScreen
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
                        .height(dimensions.bannerHeight)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    contentAlignment = Alignment.Center
                ) {
                    when (val state = itemsState) {
                        is UiState.Loading, is UiState.Empty -> {
                            CircularProgressIndicator()
                        }

                        is UiState.Success -> {
                            FoodListContent(
                                filters = state.data.filters,
                                data = state.data.sections,
                                search = state.data.search,
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    openDetailScreen(it.id)
                                },
                                onAdd = { item ->
                                    viewModel.handleEvent(
                                        PizzaListEvent.UpdateCountInCart(
                                            item.id,
                                            item.countInCart + 1
                                        )
                                    )
                                },
                                onReduce = { item ->
                                    viewModel.handleEvent(
                                        PizzaListEvent.UpdateCountInCart(
                                            item.id,
                                            (item.countInCart - 1).coerceAtLeast(0)
                                        )
                                    )
                                },
                                onSearchChange = { query ->
                                    viewModel.handleEvent(PizzaListEvent.Search(query))
                                },
                                onRemove = { item ->
                                    viewModel.handleEvent(
                                        PizzaListEvent.UpdateCountInCart(
                                            item.id,
                                            0
                                        )
                                    )
                                },
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
fun FoodListContent(
    filters: List<FoodType>,
    data: List<FoodSection>,
    search: String,
    modifier: Modifier = Modifier,
    onSearchChange: (String) -> Unit = {},
    onClick: (item: FoodItemUiModel) -> Unit = {},
    onAdd: (item: FoodItemUiModel) -> Unit = {},
    onReduce: (item: FoodItemUiModel) -> Unit = {},
    onRemove: (item: FoodItemUiModel) -> Unit = {}
) {
    val sectionHeaderIndex = remember(data) { mapSectionHeaderIndex(data) }
    Column(modifier = Modifier.fillMaxSize()) {
        LazyPizzaSearchBar(
            text = search,
            onTextChange = onSearchChange
        )
        val gridState = rememberLazyGridState()
        val scope = rememberCoroutineScope()
        val selectedFilterIndex by remember(data) {
            derivedStateOf {
                gridState.findSelectedFilterIndex(sectionHeaderIndex)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        FoodFilterChip(
            filters = filters.map { stringResource(it.getStringRes()) },
            selectedFilter = stringResource(filters[selectedFilterIndex].getStringRes()),
            onFilterSelected = { index, filter ->
                scope.launch {
                    val index = data.take(index).sumOf { it.items.size + 1 }
                    gridState.animateScrollToItem(index)
                }
            }
        )

        val dimensions = LocalDimensions.current.listScreen
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(dimensions.gridCount),
            modifier = modifier
                .padding(top = 4.dp),
        ) {
            for (section in data) {
                item(
                    key = section.type,
                    span = {
                        GridItemSpan(maxLineSpan)
                    }) {
                    Text(
                        text = stringResource(section.type.getStringRes())
                            .uppercase(),
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
                        showAddToCart = item.type != FoodType.PIZZA,
                        onClick = {
                            onClick(item)
                        },
                        onAdd = {
                            onAdd(item)
                        },
                        onRemove = {
                            onReduce(item)
                        },
                        onDelete = {
                            onRemove(item)
                        }
                    )
                }
            }
        }
    }
}

private fun mapSectionHeaderIndex(data: List<FoodSection>): IntArray {
    val array = IntArray(data.size)
    var index = 0
    for ((sectionIndex, section) in data.withIndex()) {
        array[sectionIndex] = index
        index += section.items.size + 1
    }
    return array
}

private fun LazyGridState.findSelectedFilterIndex(headerIndexList: IntArray): Int {
    val first = this.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
    val last = this.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    var nearestHeaderIndex = 0
    for ((index, headerIndex) in headerIndexList.withIndex()) {
        if (headerIndex in first..last) {
            return index
        } else if (headerIndex < first) {
            nearestHeaderIndex = index
        }
    }

    return nearestHeaderIndex
}
