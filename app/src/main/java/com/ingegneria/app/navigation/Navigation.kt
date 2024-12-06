package com.ingegneria.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ingegneria.app.ui.screens.Home
import com.ingegneria.app.ui.screens.Login
import com.ingegneria.app.ui.screens.Signup

@Composable
fun Navigation(navController: NavHostController, startScreen:String) {

    NavHost(navController = navController, startDestination = startScreen) {
        composable(Screens.Home.name) {
            Home(navController = navController)
        }
        composable(Screens.Login.name) {
            Login(navController = navController)
        }
        composable(Screens.Signup.name) {
            Signup(navController = navController)
        }
    }
}