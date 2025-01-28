package com.ingegneria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ingegneria.app.navigation.Navigation
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.otherpages.ShopViewModel
import com.ingegneria.app.ui.otherpages.SocialViewModel
import com.ingegneria.app.ui.otherpages.UserViewModel
import com.ingegneria.app.ui.screens.PetViewModel
import com.ingegneria.app.ui.tabs.TaskViewModel
import com.ingegneria.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    ProscuttoApp()
                }
            }
        }
    }
}

@Composable
fun ProscuttoApp(
    navController: NavHostController = rememberNavController(),
    userVM: UserViewModel = viewModel<UserViewModel>(),
    taskVM: TaskViewModel = viewModel<TaskViewModel>(),
    shopVM: ShopViewModel = viewModel<ShopViewModel>(),
    socialVM: SocialViewModel = viewModel<SocialViewModel>(),
    petVM: PetViewModel = viewModel<PetViewModel>()
) {
    val user = Firebase.auth.currentUser
    val startScreen =
        if (user == null) {
            Screens.Login.name
        } else {
            Screens.Home.name
        }

    val bottomBarState = rememberSaveable { mutableStateOf(true) }

    if (user != null) {
        petVM.retrieveFirebaseData(user.uid)
        shopVM.retrieveFirebaseData(user.uid, user.displayName!!)
        socialVM.retrieveFirebaseData(user.uid, user.displayName!!)
        taskVM.retrieveFirebaseData(user.uid, petVM)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        Screens.Login.name,
        Screens.Signup.name,
        Screens.Shop.name,
        Screens.Social.name,
        Screens.Stats.name,
        Screens.RequestsPage.name,
        Screens.Camera.name -> {
            bottomBarState.value = false
        }
        Screens.Home.name,
        Screens.Tasks.name,
        Screens.Quiz.name,
        Screens.Settings.name -> {
            bottomBarState.value = true
        }
    }

    Scaffold (
        bottomBar = { TabNavigationBar(navController, bottomBarState) }
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ) {
            Navigation(
                navController = navController,
                startScreen = startScreen,
                taskVM = taskVM,
                shopVM = shopVM,
                socialVM = socialVM,
                petVM = petVM,
                userVM = userVM
            )
        }
    }
}

@Composable
fun TabNavigationBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(Screens.Home.name, Screens.Tasks.name, Screens.Quiz.name, Screens.Settings.name)
    val selectedIcons = listOf(Icons.Filled.Home,
                                Icons.Filled.TaskAlt,
                                Icons.Filled.Quiz,
                                Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home,
                                 Icons.Outlined.TaskAlt,
                                 Icons.Outlined.Quiz,
                                 Icons.Outlined.Settings)

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = {it}),
        exit = slideOutVertically(targetOffsetY = {it})
    ) {
        NavigationBar {
            items.forEachIndexed { index: Int, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                            contentDescription = item
                        )
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
//    ProscuttoApp()
}