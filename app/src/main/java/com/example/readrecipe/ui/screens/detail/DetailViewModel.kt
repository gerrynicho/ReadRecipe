package com.example.readrecipe.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.repository.RecipeRepository
import com.example.readrecipe.domain.model.Meal
import com.example.readrecipe.domain.model.MealDetail
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    var mealDetail by mutableStateOf<MealDetail?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isSaved by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private var savedStateJob: Job? = null

    fun loadMeal(mealId: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            recipeRepository.getMealById(mealId)
                .onSuccess { mealDetail = it }
                .onFailure { error = it.message }
            isLoading = false
        }
        savedStateJob?.cancel()
        savedStateJob = viewModelScope.launch {
            recipeRepository.isRecipeSaved(mealId).collect { saved ->
                isSaved = saved
            }
        }
    }

    fun toggleSave() {
        val meal = mealDetail ?: return
        viewModelScope.launch {
            if (isSaved) {
                recipeRepository.unsaveMeal(meal.id)
            } else {
                recipeRepository.saveMeal(
                    Meal(
                        id = meal.id,
                        title = meal.title,
                        thumbnail = meal.thumbnail,
                        category = meal.category,
                        area = meal.area
                    )
                )
            }
        }
    }

    class Factory(private val repo: RecipeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DetailViewModel(repo) as T
    }
}
