package com.example.readrecipe.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.readrecipe.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String = "") : AuthState()
        data class Error(val message: String) : AuthState()
    }

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            authState = AuthState.Error("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            authState = AuthState.Loading
            val result = authRepository.login(email, password)
            authState = result.fold(
                onSuccess = { AuthState.Success() },
                onFailure = { AuthState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() || email.isBlank() || password.isBlank() ->
                authState = AuthState.Error("Please fill in all fields")
            password != confirmPassword ->
                authState = AuthState.Error("Passwords do not match")
            password.length < 6 ->
                authState = AuthState.Error("Password must be at least 6 characters")
            else -> viewModelScope.launch {
                authState = AuthState.Loading
                val result = authRepository.register(name, email, password)
                authState = result.fold(
                    onSuccess = { AuthState.Success() },
                    onFailure = { AuthState.Error(it.message ?: "Registration failed") }
                )
            }
        }
    }

    fun resetState() {
        authState = AuthState.Idle
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthViewModel(authRepository) as T
    }
}
