package com.ingegneria.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.firebase.auth.FirebaseUser
import com.ingegneria.app.ui.otherpages.Login
import com.ingegneria.app.ui.otherpages.Shop
import com.ingegneria.app.ui.otherpages.ShopViewModel
import com.ingegneria.app.ui.otherpages.Signup
import com.ingegneria.app.ui.otherpages.Social
import com.ingegneria.app.ui.otherpages.Stats
import com.ingegneria.app.ui.tabs.Home
import com.ingegneria.app.ui.tabs.Quiz
import com.ingegneria.app.ui.tabs.Settings
import com.ingegneria.app.ui.tabs.TaskViewModel
import com.ingegneria.app.ui.tabs.Tasks

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    startScreen:String,
    taskVM: TaskViewModel,
    shopVM: ShopViewModel,
    user: FirebaseUser?,
    cameraPermissionState: PermissionState
) {

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
            Social(navController = navController, user = user, cameraPermissionState = cameraPermissionState)
        }
        composable(Screens.Stats.name) {
            Stats(navController = navController)
        }
    }
}