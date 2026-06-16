package com.example.readrecipe.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.local.entity.SavedRecipeEntity
import com.example.readrecipe.data.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavedViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    val savedMeals: StateFlow<List<SavedRecipeEntity>> =
        recipeRepository.getSavedMeals()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun unsave(mealId: String) {
        viewModelScope.launch {
            recipeRepository.unsaveMeal(mealId)
        }
    }

    class Factory(private val repo: RecipeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SavedViewModel(repo) as T
    }
}
