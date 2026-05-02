package com.fixit.androidfront.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fixit.androidfront.ui.screens.LandingScreen
import com.fixit.androidfront.ui.screens.LoginScreen
import com.fixit.androidfront.ui.screens.HomeScreen

@Composable
fun FixItNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("landing") {
            LandingScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToHome = { navController.navigate("app") }
            )
        }
        composable("login") {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { 
                    navController.navigate("app") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            com.fixit.androidfront.ui.screens.RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = { 
                    navController.navigate("app") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }
        composable("app") {
            com.fixit.androidfront.ui.screens.HomeScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToCategory = { id -> navController.navigate("category/$id") },
                onNavigateToCreateJob = { navController.navigate("create_job") },
                onNavigateToJobDetail = { id -> navController.navigate("job_detail/$id") },
                onNavigateToChat = { navController.navigate("chat") }
            )
        }
        composable("profile") {
            com.fixit.androidfront.ui.screens.ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("create_job") {
            com.fixit.androidfront.ui.screens.CreateJobPostScreen(
                onBack = { navController.popBackStack() },
                onPostSuccess = { navController.popBackStack() }
            )
        }
        composable("job_detail/{jobId}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
            com.fixit.androidfront.ui.screens.JobPostDetailScreen(
                jobId = jobId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("chat") {
            com.fixit.androidfront.ui.screens.ChatScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("category/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            com.fixit.androidfront.ui.screens.CategoryScreen(
                categoryId = categoryId,
                onBack = { navController.popBackStack() },
                onAdClick = { id -> navController.navigate("job_detail/$id") }
            )
        }
    }
}
