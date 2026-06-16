package com.example.readrecipe.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.local.entity.UserEntity
import com.example.readrecipe.data.repository.AuthRepository
import com.example.readrecipe.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    var currentUser by mutableStateOf<UserEntity?>(null)
        private set
    var savedCount by mutableIntStateOf(0)
        private set

    init {
        viewModelScope.launch {
            currentUser = authRepository.getCurrentUser()
        }
        viewModelScope.launch {
            recipeRepository.getSavedMeals().collect { list ->
                savedCount = list.size
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    class Factory(
        private val authRepository: AuthRepository,
        private val recipeRepository: RecipeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProfileViewModel(authRepository, recipeRepository) as T
    }
}
