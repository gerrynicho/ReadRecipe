package com.example.readrecipe.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.readrecipe.ReadRecipeApplication
import com.example.readrecipe.ui.screens.auth.AuthViewModel
import com.example.readrecipe.ui.screens.auth.LoginScreen
import com.example.readrecipe.ui.screens.auth.SignUpScreen
import com.example.readrecipe.ui.screens.detail.DetailViewModel
import com.example.readrecipe.ui.screens.detail.RecipeDetailScreen
import com.example.readrecipe.ui.screens.home.HomeScreen
import com.example.readrecipe.ui.screens.home.HomeViewModel
import com.example.readrecipe.ui.screens.profile.ProfileScreen
import com.example.readrecipe.ui.screens.profile.ProfileViewModel
import com.example.readrecipe.ui.screens.saved.SavedScreen
import com.example.readrecipe.ui.screens.saved.SavedViewModel
import com.example.readrecipe.ui.screens.search.SearchScreen
import com.example.readrecipe.ui.screens.search.SearchViewModel

private val bottomNavRoutes = setOf("home", "search", "saved", "profile")

@Composable
fun NavGraph() {
    val context = LocalContext.current
    val app = context.applicationContext as ReadRecipeApplication

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: ""

    val startDestination = if (app.sessionManager.isLoggedIn()) "home" else "login"

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable("login") {
                val authVm: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(app.authRepository)
                )
                LoginScreen(
                    viewModel = authVm,
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate("signup")
                    }
                )
            }

            composable("signup") {
                val authVm: AuthViewModel = viewModel(
                    factory = AuthViewModel.Factory(app.authRepository)
                )
                SignUpScreen(
                    viewModel = authVm,
                    onSignUpSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable("home") {
                val homeVm: HomeViewModel = viewModel(
                    factory = HomeViewModel.Factory(app.recipeRepository, app.authRepository)
                )
                HomeScreen(
                    viewModel = homeVm,
                    onRecipeClick = { mealId ->
                        navController.navigate("detail/$mealId")
                    }
                )
            }

            composable("search") {
                val searchVm: SearchViewModel = viewModel(
                    factory = SearchViewModel.Factory(app.recipeRepository)
                )
                SearchScreen(
                    viewModel = searchVm,
                    onRecipeClick = { mealId ->
                        navController.navigate("detail/$mealId")
                    }
                )
            }

            composable("saved") {
                val savedVm: SavedViewModel = viewModel(
                    factory = SavedViewModel.Factory(app.recipeRepository)
                )
                SavedScreen(
                    viewModel = savedVm,
                    onRecipeClick = { mealId ->
                        navController.navigate("detail/$mealId")
                    }
                )
            }

            composable("profile") {
                val profileVm: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.Factory(app.authRepository, app.recipeRepository)
                )
                ProfileScreen(
                    viewModel = profileVm,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable("detail/{mealId}") { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable
                val detailVm: DetailViewModel = viewModel(
                    factory = DetailViewModel.Factory(app.recipeRepository)
                )
                RecipeDetailScreen(
                    mealId = mealId,
                    viewModel = detailVm,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
