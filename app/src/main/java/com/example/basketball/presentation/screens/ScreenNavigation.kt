package com.example.basketball.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ScreenNavigation() {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = LOGIN_SCREEN) {
        composable(LOGIN_SCREEN) {
            OnboardingScreen(navHostController){
                navHostController.navigate(DASHBOARD_SCREEN)
            }
        }
        composable(DASHBOARD_SCREEN) {
            DashBoardScreen(navHostController)
        }
    }
}

const val LOGIN_SCREEN = "login_screen"
const val DASHBOARD_SCREEN = "dashboard_screen"
