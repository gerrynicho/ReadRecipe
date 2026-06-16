package com.example.readrecipe.ui.screens.saved

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.ui.components.RecipeCard
import com.example.readrecipe.ui.theme.DarkText
import com.example.readrecipe.ui.theme.GrayText

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onRecipeClick: (String) -> Unit
) {
    val savedMeals by viewModel.savedMeals.collectAsState()

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Saved Recipes",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                if (savedMeals.isEmpty()) "No saved recipes yet"
                else "${savedMeals.size} recipe${if (savedMeals.size == 1) "" else "s"} saved",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText
            )
            Spacer(modifier = Modifier.height(20.dp))

            if (savedMeals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❤️", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No saved recipes",
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkText,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tap the heart on any recipe to save it",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(savedMeals, key = { it.mealId }) { entity ->
                        val meal = Meal(
                            id = entity.mealId,
                            title = entity.title,
                            thumbnail = entity.thumbnail,
                            category = entity.category
                        )
                        RecipeCard(
                            meal = meal,
                            isSaved = true,
                            onRecipeClick = { onRecipeClick(entity.mealId) },
                            onSaveClick = { viewModel.unsave(entity.mealId) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}
