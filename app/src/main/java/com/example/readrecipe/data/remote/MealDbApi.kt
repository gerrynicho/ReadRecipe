package com.example.readrecipe.data.remote

import com.example.readrecipe.data.remote.model.CategoryResponse
import com.example.readrecipe.data.remote.model.MealDetailResponse
import com.example.readrecipe.data.remote.model.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApi {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealDetailResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealDetailResponse
}
