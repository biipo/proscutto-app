package com.ingegneria.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ingegneria.app.ui.screens.Home
import com.ingegneria.app.ui.screens.Login
import com.ingegneria.app.ui.screens.Quiz
import com.ingegneria.app.ui.screens.Settings
import com.ingegneria.app.ui.screens.Signup
import com.ingegneria.app.ui.screens.Tasks

@Composable
fun Navigation(navController: NavHostController, startScreen:String) {

    NavHost(navController = navController, startDestination = startScreen) {
        composable(Screens.Login.name) {
            Login(navController = navController)
        }
        composable(Screens.Signup.name) {
            Signup(navController = navController)
        }
        // TODO: forgot password
        composable(Screens.Home.name) {
            Home(navController = navController)
        }
        composable(Screens.Tasks.name) {
            Tasks(navController = navController)
        }
        composable(Screens.Quiz.name) {
            Quiz(navController = navController)
        }
        composable(Screens.Settings.name) {
            Settings(navController = navController)
        }
//        composable(Screens.Quiz.name) {
//            Social(navController = navController)
//        }
//        composable(Screens.Quiz.name) {
//            Shop(navController = navController)
//        }
//        composable(Screens.Quiz.name) {
//            Stats(navController = navController)
//        }
    }
}