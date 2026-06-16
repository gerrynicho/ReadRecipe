package com.example.readrecipe.domain.model

data class Meal(
    val id: String,
    val title: String,
    val thumbnail: String,
    val category: String = "",
    val area: String = ""
)
