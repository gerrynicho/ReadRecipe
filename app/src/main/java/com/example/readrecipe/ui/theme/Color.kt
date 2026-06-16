package com.example.readrecipe.ui.theme

import androidx.compose.ui.graphics.Color

// Primary accent
val SoftOrange = Color(0xFFE8734A)
val SoftOrangeLight = Color(0xFFFAE3DB)

// Text colors
val DarkText = Color(0xFF2D2D2D)
val GrayText = Color(0xFF9E9E9E)

// UI elements
val ChipBorder = Color(0xFFE0E0E0)
val CardBackground = Color(0xFFFFFFFF)

// Category badge colors (TheMealDB categories)
val CategoryBeef = Color(0xFFE8734A)
val CategoryChicken = Color(0xFFFF9800)
val CategorySeafood = Color(0xFF03A9F4)
val CategoryVegetarian = Color(0xFF4CAF50)
val CategoryVegan = Color(0xFF8BC34A)
val CategoryDessert = Color(0xFF9C27B0)
val CategoryPasta = Color(0xFFFF5722)
val CategoryLamb = Color(0xFF795548)
val CategoryPork = Color(0xFFF44336)
val CategoryBreakfast = Color(0xFFE8734A)
val CategoryStarter = Color(0xFF009688)
val CategorySide = Color(0xFF607D8B)
val CategoryGoat = Color(0xFFFF9800)
val CategoryMiscellaneous = Color(0xFF9E9E9E)

// Featured card gradient
val WarmGradStart = Color(0xFFE8DFC8)
val WarmGradEnd = Color(0xFFD4C9A8)

fun getCategoryColor(category: String): Color = when (category.lowercase()) {
    "beef" -> CategoryBeef
    "chicken" -> CategoryChicken
    "seafood" -> CategorySeafood
    "vegetarian" -> CategoryVegetarian
    "vegan" -> CategoryVegan
    "dessert" -> CategoryDessert
    "pasta" -> CategoryPasta
    "lamb" -> CategoryLamb
    "pork" -> CategoryPork
    "breakfast" -> CategoryBreakfast
    "starter" -> CategoryStarter
    "side" -> CategorySide
    "goat" -> CategoryGoat
    else -> CategoryMiscellaneous
}
