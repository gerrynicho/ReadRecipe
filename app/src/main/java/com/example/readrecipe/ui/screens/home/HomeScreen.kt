package com.example.readrecipe.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.readrecipe.ui.components.CategoryChip
import com.example.readrecipe.ui.components.FeaturedRecipeCarousel
import com.example.readrecipe.ui.components.RecipeCard
import com.example.readrecipe.ui.components.toFeaturedRecipeItem
import com.example.readrecipe.ui.theme.DarkText
import com.example.readrecipe.ui.theme.GrayText
import com.example.readrecipe.ui.theme.SoftOrange

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onRecipeClick: (String) -> Unit,
    onProfileClick: () -> Unit = {}
) {
    val currentUser = viewModel.currentUser
    val categories = viewModel.categories
    val selectedCategory = viewModel.selectedCategory
    val meals = viewModel.mealsForCategory
    val featuredMeals = viewModel.featuredMeals
    val isLoading = viewModel.isLoadingMeals
    val savedMealIds = viewModel.savedMealIds

    val userName = currentUser?.name ?: "Food Lover"
    val featuredRecipeItems = if (featuredMeals.isNotEmpty()) {
        featuredMeals.map { it.toFeaturedRecipeItem() }
    } else {
        meals.take(5).map { it.toFeaturedRecipeItem() }
    }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Good morning 👋",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                        Text(
                            userName,
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(SoftOrange.copy(alpha = 0.15f), CircleShape)
                            .clickable { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.avatarEmoji ?: "👤",
                            fontSize = 22.sp
                        )
                    }
                }

                // Featured section
                if (featuredRecipeItems.isNotEmpty()) {
                    Text(
                        "Featured Recipes",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FeaturedRecipeCarousel(
                        meals = featuredRecipeItems,
                        onClick = { mealId -> onRecipeClick(mealId) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Categories
                if (categories.isNotEmpty()) {
                    Text(
                        "Categories",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryChip(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Meals section header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        if (selectedCategory.isNotEmpty()) selectedCategory else "All Recipes",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText,
                        fontWeight = FontWeight.Bold
                    )
                    if (meals.isNotEmpty()) {
                        Text(
                            "${meals.size} recipes",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrayText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SoftOrange)
                    }
                } else {
                    val gridHeight = (((meals.size + 1) / 2) * 220 + 20).coerceAtLeast(220)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gridHeight.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        userScrollEnabled = false
                    ) {
                        items(meals) { meal ->
                            RecipeCard(
                                meal = meal,
                                isSaved = savedMealIds.contains(meal.id),
                                onRecipeClick = { onRecipeClick(meal.id) },
                                onSaveClick = { viewModel.toggleSave(meal) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
