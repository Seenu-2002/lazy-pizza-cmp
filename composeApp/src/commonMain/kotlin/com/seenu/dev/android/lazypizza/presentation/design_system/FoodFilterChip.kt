package com.seenu.dev.android.lazypizza.presentation.design_system

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.unit.dp
import com.seenu.dev.android.lazypizza.presentation.theme.LazyPizzaTheme
import com.seenu.dev.android.lazypizza.presentation.theme.body3Medium
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun FoodFilterChipPreview() {
    LazyPizzaTheme {
        val filters = listOf("Pizza", "Burger", "Pasta", "Dessert", "Drinks")
        FoodFilterChip(
            filters = filters,
            selectedFilter = "Pizza",
            onFilterSelected = { _, _ -> }
        )
    }
}

@Composable
fun FoodFilterChip(
    modifier: Modifier = Modifier,
    filters: List<String>,
    selectedFilter: String?,
    onFilterSelected: (Int, String) -> Unit
) {

    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(filters.size) { index ->
            val filter = filters[index]
            FoodFilterChipItem(
                modifier = modifier.padding(end = 8.dp),
                isSelected = filter == selectedFilter,
                text = filter,
                onClick = { onFilterSelected(index, filter) }
            )
        }
    }

}

@Preview
@Composable
private fun FoodFilterChipItemSelectedPreview() {
    LazyPizzaTheme {
        FoodFilterChipItem(
            isSelected = true,
            text = "Pizza",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FoodFilterChipItemPreview() {
    LazyPizzaTheme {
        FoodFilterChipItem(
            isSelected = false,
            text = "Pizza",
            onClick = {}
        )
    }
}

@Composable
fun FoodFilterChipItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    val shape = MaterialTheme.shapes.small
    Box(
        modifier = modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body3Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}