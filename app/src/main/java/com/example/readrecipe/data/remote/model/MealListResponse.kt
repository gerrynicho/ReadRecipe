package com.example.readrecipe.data.remote.model

import com.google.gson.annotations.SerializedName

data class MealListResponse(
    @SerializedName("meals") val meals: List<MealSummaryDto>?
)

data class MealSummaryDto(
    @SerializedName("idMeal") val idMeal: String,
    @SerializedName("strMeal") val strMeal: String,
    @SerializedName("strMealThumb") val strMealThumb: String
)
