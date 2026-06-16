package com.example.readrecipe.data.repository

import com.example.readrecipe.data.local.dao.SavedRecipeDao
import com.example.readrecipe.data.local.entity.SavedRecipeEntity
import com.example.readrecipe.data.remote.MealDbApi
import com.example.readrecipe.data.remote.model.MealDetailDto
import com.example.readrecipe.domain.model.Ingredient
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.domain.model.MealDetail
import com.example.readrecipe.session.SessionManager
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val api: MealDbApi,
    private val savedRecipeDao: SavedRecipeDao,
    private val sessionManager: SessionManager
) {
    suspend fun getCategories(): Result<List<String>> = try {
        val response = api.getCategories()
        Result.success(response.categories.map { it.strCategory })
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMealsByCategory(category: String): Result<List<Meal>> = try {
        val response = api.getMealsByCategory(category)
        val meals = response.meals?.map { dto ->
            Meal(id = dto.idMeal, title = dto.strMeal, thumbnail = dto.strMealThumb, category = category)
        } ?: emptyList()
        Result.success(meals)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun searchMeals(query: String): Result<List<Meal>> = try {
        val response = api.searchMeals(query)
        val meals = response.meals?.map { dto ->
            Meal(id = dto.idMeal, title = dto.strMeal, thumbnail = dto.strMealThumb)
        } ?: emptyList()
        Result.success(meals)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getMealById(id: String): Result<MealDetail> = try {
        val dto = api.getMealById(id).meals?.firstOrNull()
            ?: return Result.failure(Exception("Meal not found"))
        Result.success(dto.toMealDetail())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getRandomMeal(): Result<MealDetail> = try {
        val dto = api.getRandomMeal().meals?.firstOrNull()
            ?: return Result.failure(Exception("No meal returned"))
        Result.success(dto.toMealDetail())
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getSavedMeals(): Flow<List<SavedRecipeEntity>> {
        val userId = sessionManager.getLoggedInUserId()
        return savedRecipeDao.getSavedRecipes(userId)
    }

    fun isRecipeSaved(mealId: String): Flow<Boolean> {
        val userId = sessionManager.getLoggedInUserId()
        return savedRecipeDao.isRecipeSaved(mealId, userId)
    }

    suspend fun saveMeal(meal: Meal) {
        val userId = sessionManager.getLoggedInUserId()
        if (userId == -1L) return
        savedRecipeDao.save(
            SavedRecipeEntity(
                mealId = meal.id,
                userId = userId,
                title = meal.title,
                thumbnail = meal.thumbnail,
                category = meal.category
            )
        )
    }

    suspend fun unsaveMeal(mealId: String) {
        val userId = sessionManager.getLoggedInUserId()
        if (userId == -1L) return
        savedRecipeDao.unsave(mealId, userId)
    }

    private fun MealDetailDto.toMealDetail(): MealDetail {
        return MealDetail(
            id = idMeal,
            title = strMeal,
            category = strCategory,
            area = strArea,
            instructions = strInstructions,
            thumbnail = strMealThumb,
            youtubeUrl = strYoutube?.takeIf { it.isNotBlank() },
            tags = getTags(),
            ingredients = getIngredients().map { (name, measure) -> Ingredient(name, measure) },
            steps = getSteps()
        )
    }
}
