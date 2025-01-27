package com.ingegneria.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ingegneria.app.ui.common.QrReader
import com.ingegneria.app.ui.otherpages.ForgotPassword
import com.ingegneria.app.ui.otherpages.FriendRequests
import com.ingegneria.app.ui.otherpages.Login
import com.ingegneria.app.ui.otherpages.Shop
import com.ingegneria.app.ui.otherpages.ShopViewModel
import com.ingegneria.app.ui.otherpages.Signup
import com.ingegneria.app.ui.otherpages.Social
import com.ingegneria.app.ui.otherpages.SocialViewModel
import com.ingegneria.app.ui.otherpages.Stats
import com.ingegneria.app.ui.screens.PetViewModel
import com.ingegneria.app.ui.otherpages.UserViewModel
import com.ingegneria.app.ui.tabs.Home
import com.ingegneria.app.ui.tabs.Quiz
import com.ingegneria.app.ui.tabs.Settings
import com.ingegneria.app.ui.tabs.TaskViewModel
import com.ingegneria.app.ui.tabs.Tasks
import com.ingegneria.app.ui.otherpages.ForgotPassword

@Composable
fun Navigation(
    navController: NavHostController,
    startScreen:String,
    taskVM: TaskViewModel,
    shopVM: ShopViewModel,
    socialVM: SocialViewModel,
    petVM: PetViewModel,
    userVM: UserViewModel
) {

    NavHost(navController = navController, startDestination = startScreen) {
        composable(Screens.Login.name) {
            Login(navController = navController, userVM = userVM)
        }
        composable(Screens.Signup.name) {
            Signup(navController = navController, userVM = userVM)
        }
        composable(Screens.ForgotPassword.name){
            ForgotPassword(navController = navController)
        }
        composable(Screens.Home.name) {
            Home(navController = navController, petVM = petVM)
        }
        composable(Screens.Tasks.name) {
            Tasks(navController = navController, taskVM = taskVM, petVM = petVM)
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
        composable( // This is used to navigate with arguments (they are optional)
            "${Screens.Social.name}?qrValue={qrValue}",
            arguments = listOf(
                navArgument("qrValue") {
                    type = NavType.StringType
                    defaultValue = ""
                })
        ) {
            Social(navController = navController, socialVM = socialVM, qrValue = it.arguments?.getString("qrValue"))
        }
        composable(Screens.Stats.name) {
            Stats(navController = navController)
        }
        composable(Screens.Camera.name) {
            QrReader(navController = navController)
        }
        composable(Screens.RequestsPage.name) {
            FriendRequests(navController = navController, socialVM = socialVM)
        }
    }
}