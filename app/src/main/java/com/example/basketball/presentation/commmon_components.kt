package com.example.basketball.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.basketball.data.model.FilterCategory
import kotlinx.coroutines.launch


@Composable
fun ListDivider() {
    Divider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun ShowLoading(modifier: Modifier = Modifier) {
    val strokeWidth = 5.dp
    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 80.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color.White,
                strokeWidth = strokeWidth
            )

            SpacerWidth(15.dp)
            Text(
                text = "Loading..",
                modifier = modifier,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
fun FilterBottomSheet(
    filterCategories: List<FilterCategory>,
    onCategorySelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var selectedCategories by remember { mutableStateOf(filterCategories) }
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Filter Categories", style = MaterialTheme.typography.bodyMedium)
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    selectedCategories.forEach { category ->
                        Chip(
                            category = category,
                            onCategorySelected = { name ->
                                selectedCategories = selectedCategories.map {
                                    if (it.name == name) it.copy(isSelected = !it.isSelected) else it
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            selectedCategories.filter { it.isSelected }.forEach {
                                onCategorySelected(it.name)
                            }
                            onDismissRequest()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun Chip(
    category: FilterCategory,
    onCategorySelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onCategorySelected(category.name) }
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (category.isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    ) {
        Text(
            text = category.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White
        )
    }
}
