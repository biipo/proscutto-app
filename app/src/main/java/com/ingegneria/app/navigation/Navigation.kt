package com.ingegneria.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ingegneria.app.otherpages.Shop
import com.ingegneria.app.otherpages.ShopViewModel
import com.ingegneria.app.otherpages.Social
import com.ingegneria.app.ui.screens.Home
import com.ingegneria.app.ui.screens.Login
import com.ingegneria.app.ui.screens.Quiz
import com.ingegneria.app.ui.screens.Settings
import com.ingegneria.app.ui.screens.Signup
import com.ingegneria.app.ui.screens.TaskViewModel
import com.ingegneria.app.ui.screens.Tasks

@Composable
fun Navigation(navController: NavHostController, startScreen:String) {

    val taskVM = viewModel<TaskViewModel>()
    val shopVM = viewModel<ShopViewModel>()
    taskVM.retrieveFirebaseData()
    shopVM.retrieveFirebaseData()

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
            Tasks(navController = navController, taskVM = taskVM)
        }
        composable(Screens.Quiz.name) {
            Quiz(navController = navController)
        }
        composable(Screens.Settings.name) {
            Settings(navController = navController)
        }
        composable(Screens.Shop.name) {
            Shop(navController = navController, shopVM = shopVM)
        }
        composable(Screens.Social.name) {
            Social(navController = navController)
        }
//        composable(Screens.Quiz.name) {
//            Stats(navController = navController)
//        }
    }
}