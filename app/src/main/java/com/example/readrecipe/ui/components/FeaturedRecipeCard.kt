package com.example.readrecipe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.domain.model.MealDetail
import com.example.readrecipe.ui.theme.DarkText
import com.example.readrecipe.ui.theme.SoftOrange
import com.example.readrecipe.ui.theme.WarmGradEnd
import com.example.readrecipe.ui.theme.WarmGradStart
import kotlinx.coroutines.delay

data class FeaturedRecipeItem(
    val id: String,
    val title: String,
    val category: String,
    val area: String,
    val thumbnail: String
)

fun MealDetail.toFeaturedRecipeItem(): FeaturedRecipeItem =
    FeaturedRecipeItem(
        id = id,
        title = title,
        category = category,
        area = area,
        thumbnail = thumbnail
    )

fun Meal.toFeaturedRecipeItem(): FeaturedRecipeItem =
    FeaturedRecipeItem(
        id = id,
        title = title,
        category = category,
        area = area,
        thumbnail = thumbnail
    )

@Composable
fun FeaturedRecipeCarousel(
    meals: List<FeaturedRecipeItem>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (meals.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { meals.size })

    LaunchedEffect(meals.size) {
        if (meals.size <= 1) return@LaunchedEffect

        while (true) {
            delay(4500)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % meals.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            FeaturedRecipeCard(
                meal = meals[page],
                onClick = onClick
            )
        }
    }
}

@Composable
fun FeaturedRecipeCard(
    meal: FeaturedRecipeItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick(meal.id) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(WarmGradStart, WarmGradEnd)
                    )
                )
        ) {
            AsyncImage(
                model = meal.thumbnail,
                contentDescription = meal.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .padding(14.dp)
                    .background(SoftOrange, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "FEATURED",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Text(
                    text = meal.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = buildString {
                        if (meal.area.isNotBlank()) append(meal.area)
                        if (meal.area.isNotBlank() && meal.category.isNotBlank()) append(" · ")
                        if (meal.category.isNotBlank()) append(meal.category)
                    },
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}
