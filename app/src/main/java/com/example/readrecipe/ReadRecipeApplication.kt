package com.example.readrecipe

import android.app.Application
import com.example.readrecipe.data.local.AppDatabase
import com.example.readrecipe.data.remote.RetrofitInstance
import com.example.readrecipe.data.repository.AuthRepository
import com.example.readrecipe.data.repository.RecipeRepository
import com.example.readrecipe.session.SessionManager

class ReadRecipeApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val sessionManager by lazy { SessionManager(this) }

    val authRepository by lazy {
        AuthRepository(database.userDao(), sessionManager)
    }

    val recipeRepository by lazy {
        RecipeRepository(
            RetrofitInstance.api,
            database.savedRecipeDao(),
            sessionManager
        )
    }
}
