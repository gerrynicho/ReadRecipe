package com.example.readrecipe.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.repository.RecipeRepository
import com.example.readrecipe.domain.model.Meal
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set
    var results by mutableStateOf<List<Meal>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var savedMealIds by mutableStateOf<Set<String>>(emptySet())
        private set

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            recipeRepository.getSavedMeals().collect { list ->
                savedMealIds = list.map { it.mealId }.toSet()
            }
        }
    }

    fun onQueryChange(query: String) {
        searchQuery = query
        searchJob?.cancel()
        if (query.isBlank()) {
            results = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(500L)
            isLoading = true
            recipeRepository.searchMeals(query)
                .onSuccess { results = it }
            isLoading = false
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

    class Factory(private val repo: RecipeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(repo) as T
    }
}
