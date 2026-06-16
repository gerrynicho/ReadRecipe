package com.example.readrecipe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.ui.theme.DarkText
import com.example.readrecipe.ui.theme.GrayText
import com.example.readrecipe.ui.theme.SoftOrange
import com.example.readrecipe.ui.theme.getCategoryColor

@Composable
fun RecipeCard(
    meal: Meal,
    isSaved: Boolean,
    onRecipeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRecipeClick(meal.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.18f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.Center
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
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.05f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.45f)
                                )
                            )
                        )
                )

                if (meal.category.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .background(
                                color = getCategoryColor(meal.category).copy(alpha = 0.92f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = meal.category,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.92f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onSaveClick(meal.id) },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isSaved) "Unsave" else "Save",
                            tint = if (isSaved) SoftOrange else DarkText.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = meal.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                if (meal.area.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meal.area,
                        fontSize = 12.sp,
                        color = GrayText,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
