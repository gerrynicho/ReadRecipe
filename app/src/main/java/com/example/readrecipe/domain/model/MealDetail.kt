package com.example.readrecipe.domain.model

data class Ingredient(val name: String, val measure: String) {
    val imageUrl: String
        get() = "https://www.themealdb.com/images/ingredients/${name.trim()}-Small.png"
}

data class MealDetail(
    val id: String,
    val title: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnail: String,
    val youtubeUrl: String?,
    val tags: List<String>,
    val ingredients: List<Ingredient>,
    val steps: List<String>
)
