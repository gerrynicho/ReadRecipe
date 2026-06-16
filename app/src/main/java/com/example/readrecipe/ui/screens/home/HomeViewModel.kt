package com.example.readrecipe.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.local.entity.UserEntity
import com.example.readrecipe.data.repository.AuthRepository
import com.example.readrecipe.data.repository.RecipeRepository
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.domain.model.MealDetail
import kotlinx.coroutines.launch

class HomeViewModel(
    private val recipeRepository: RecipeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var categories by mutableStateOf<List<String>>(emptyList())
        private set
    var selectedCategory by mutableStateOf("")
        private set
    var mealsForCategory by mutableStateOf<List<Meal>>(emptyList())
        private set
    var featuredMeals by mutableStateOf<List<MealDetail>>(emptyList())
        private set
    var currentUser by mutableStateOf<UserEntity?>(null)
        private set
    var isLoadingCategories by mutableStateOf(false)
        private set
    var isLoadingMeals by mutableStateOf(false)
        private set
    var savedMealIds by mutableStateOf<Set<String>>(emptySet())
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadCurrentUser()
        loadInitialData()
        collectSavedIds()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            currentUser = authRepository.getCurrentUser()
        }
    }

    private fun collectSavedIds() {
        viewModelScope.launch {
            recipeRepository.getSavedMeals().collect { list ->
                savedMealIds = list.map { it.mealId }.toSet()
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            isLoadingCategories = true
            recipeRepository.getCategories()
                .onSuccess { cats ->
                    categories = cats
                    val first = cats.firstOrNull()
                    if (first != null) {
                        selectedCategory = first
                        loadMealsForCategory(first)
                    }
                }
                .onFailure { error = it.message }
            isLoadingCategories = false

            recipeRepository.getRandomMeals(FEATURED_MEAL_COUNT)
                .onSuccess { featuredMeals = it }
        }
    }

    fun selectCategory(category: String) {
        if (category == selectedCategory) return
        selectedCategory = category
        loadMealsForCategory(category)
    }

    private fun loadMealsForCategory(category: String) {
        viewModelScope.launch {
            isLoadingMeals = true
            recipeRepository.getMealsByCategory(category)
                .onSuccess { mealsForCategory = it }
                .onFailure { error = it.message }
            isLoadingMeals = false
        }
    }

    fun toggleSave(meal: Meal) {
        viewModelScope.launch {
            if (savedMealIds.contains(meal.id)) {
                recipeRepository.unsaveMeal(meal.id)
            } else {
                recipeRepository.saveMeal(meal)
            }
        }
    }

    fun refreshFeatured() {
        viewModelScope.launch {
            recipeRepository.getRandomMeals(FEATURED_MEAL_COUNT)
                .onSuccess { featuredMeals = it }
        }
    }

    class Factory(
        private val recipeRepository: RecipeRepository,
        private val authRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(recipeRepository, authRepository) as T
    }

    private companion object {
        const val FEATURED_MEAL_COUNT = 5
    }
}
