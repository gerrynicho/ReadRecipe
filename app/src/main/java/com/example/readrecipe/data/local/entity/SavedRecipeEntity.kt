package com.example.readrecipe.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "saved_recipes",
    primaryKeys = ["meal_id", "user_id"]
)
data class SavedRecipeEntity(
    @ColumnInfo(name = "meal_id") val mealId: String,
    @ColumnInfo(name = "user_id") val userId: Long,
    val title: String,
    val thumbnail: String,
    val category: String
)
