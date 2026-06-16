package com.example.readrecipe.ui.screens.detail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.readrecipe.domain.model.Ingredient
import com.example.readrecipe.domain.model.MealDetail
import com.example.readrecipe.ui.theme.ChipBorder
import com.example.readrecipe.ui.theme.DarkText
import com.example.readrecipe.ui.theme.GrayText
import com.example.readrecipe.ui.theme.SoftOrange

@Composable
fun RecipeDetailScreen(
    mealId: String,
    viewModel: DetailViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(mealId) {
        viewModel.loadMeal(mealId)
    }

    val mealDetail = viewModel.mealDetail
    val isLoading = viewModel.isLoading
    val isSaved = viewModel.isSaved
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedIngredient by remember { mutableStateOf<Ingredient?>(null) }
    var showCookingMode by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color.White) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SoftOrange)
            }
            return@Scaffold
        }

        val meal = mealDetail ?: return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                AsyncImage(
                    model = meal.thumbnail,
                    contentDescription = meal.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.55f)
                            )
                        )
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                IconButton(
                    onClick = { viewModel.toggleSave() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isSaved) "Unsave" else "Save",
                        tint = if (isSaved) SoftOrange else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                // Tags
                if (meal.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        meal.tags.take(4).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .background(SoftOrange.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    tag,
                                    color = SoftOrange,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    meal.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = DarkText,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val meta = buildString {
                    if (meal.area.isNotBlank()) append("🌍 ${meal.area}")
                    if (meal.area.isNotBlank() && meal.category.isNotBlank()) append(" · ")
                    if (meal.category.isNotBlank()) append(meal.category)
                }
                if (meta.isNotEmpty()) {
                    Text(meta, style = MaterialTheme.typography.bodyMedium, color = GrayText)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { showCookingMode = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftOrange)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Start Cooking Mode",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = SoftOrange,
                    edgePadding = 0.dp,
                    divider = { HorizontalDivider(color = ChipBorder) },
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = SoftOrange
                        )
                    }
                ) {
                    listOf("Ingredients", "Steps").forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (selectedTab == index) SoftOrange else GrayText
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                when (selectedTab) {
                    0 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                meal.ingredients.forEachIndexed { index, ingredient ->
                                    if (index > 0) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            color = ChipBorder.copy(alpha = 0.5f)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { selectedIngredient = ingredient }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = ingredient.imageUrl,
                                            contentDescription = ingredient.name,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentScale = ContentScale.Fit
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            ingredient.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = DarkText,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            ingredient.measure,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = SoftOrange,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            meal.steps.forEachIndexed { index, step ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(SoftOrange, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        step,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = DarkText,
                                        modifier = Modifier.weight(1f),
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }
                }

                if (!meal.youtubeUrl.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(28.dp))
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtubeUrl))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SoftOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = SoftOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Watch on YouTube",
                            color = SoftOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        selectedIngredient?.let { ingredient ->
            IngredientImageDialog(
                ingredient = ingredient,
                onDismiss = { selectedIngredient = null }
            )
        }

        if (showCookingMode) {
            CookingModeDialog(
                meal = meal,
                onDismiss = { showCookingMode = false }
            )
        }
    }
}

@Composable
private fun IngredientImageDialog(
    ingredient: Ingredient,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        ingredient.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = GrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8F8F8)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ingredient.imageUrl,
                        contentDescription = ingredient.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                if (ingredient.measure.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        ingredient.measure,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SoftOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CookingModeDialog(
    meal: MealDetail,
    onDismiss: () -> Unit
) {
    var checkedIngredients by remember(meal.id) { mutableStateOf<Set<Int>>(emptySet()) }
    var currentStep by remember(meal.id) { mutableIntStateOf(0) }
    var selectedCookingSession by remember(meal.id) { mutableIntStateOf(0) }
    val totalIngredients = meal.ingredients.size
    val checkedCount = checkedIngredients.size
    val ingredientProgress = if (totalIngredients == 0) 1f else checkedCount / totalIngredients.toFloat()
    val steps = meal.steps.ifEmpty { listOf(meal.instructions) }
    val step = steps.getOrNull(currentStep).orEmpty()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Cooking Mode",
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            meal.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = GrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F8F8), RoundedCornerShape(16.dp))
                        .padding(5.dp)
                ) {
                    listOf("Ingredients", "Steps").forEachIndexed { index, title ->
                        val selected = selectedCookingSession == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selected) SoftOrange else Color.Transparent)
                                .clickable { selectedCookingSession = index }
                                .padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                title,
                                color = if (selected) Color.White else GrayText,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF8F8F8))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        if (selectedCookingSession == 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Ingredients Checklist",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DarkText,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "$checkedCount/$totalIngredients",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoftOrange,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { ingredientProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = SoftOrange,
                                trackColor = ChipBorder.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            meal.ingredients.forEachIndexed { index, ingredient ->
                                val checked = checkedIngredients.contains(index)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            checkedIngredients = if (checked) {
                                                checkedIngredients - index
                                            } else {
                                                checkedIngredients + index
                                            }
                                        }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { isChecked ->
                                            checkedIngredients = if (isChecked) {
                                                checkedIngredients + index
                                            } else {
                                                checkedIngredients - index
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = SoftOrange,
                                            uncheckedColor = GrayText
                                        )
                                    )
                                    Text(
                                        ingredient.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (checked) GrayText else DarkText,
                                        textDecoration = if (checked) TextDecoration.LineThrough else null,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (ingredient.measure.isNotBlank()) {
                                        Text(
                                            ingredient.measure,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SoftOrange,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                "Step ${currentStep + 1} of ${steps.size}",
                                style = MaterialTheme.typography.labelLarge,
                                color = SoftOrange,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                step,
                                style = MaterialTheme.typography.bodyLarge,
                                color = DarkText,
                                lineHeight = 26.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedCookingSession == 0) {
                        Text(
                            "$checkedCount of $totalIngredients ready",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText,
                            fontWeight = FontWeight.SemiBold
                        )
                        Button(
                            onClick = { selectedCookingSession = 1 },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftOrange),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Start Steps",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        TextButton(
                            onClick = { if (currentStep > 0) currentStep-- },
                            enabled = currentStep > 0
                        ) {
                            Text("Previous")
                        }

                        Text(
                            "${currentStep + 1}/${steps.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText,
                            fontWeight = FontWeight.SemiBold
                        )

                        Button(
                            onClick = {
                                if (currentStep < steps.lastIndex) {
                                    currentStep++
                                } else {
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftOrange),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (currentStep < steps.lastIndex) "Next" else "Finish",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
