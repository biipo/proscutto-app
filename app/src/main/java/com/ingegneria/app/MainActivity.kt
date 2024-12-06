package com.ingegneria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ingegneria.app.navigation.Navigation
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProscuttoApp()
                }
            }
        }
        //auth = Firebase.auth

        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        // Useful for understanding navigation: https://www.youtube.com/watch?v=Y0Cs2MQxyIs
        //val navView: BottomNavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //navView.setupWithNavController(navController)
    }
}

@Composable
fun ProscuttoApp(navController: NavHostController = rememberNavController()) {
    val firebaseUser = Firebase.auth.currentUser
    var selectedItem by remember { mutableIntStateOf(0) }
    val startScreen =
        if (firebaseUser == null) {
            Screens.Login.name
        } else {
            Screens.Home.name
        }
    Scaffold (
        bottomBar = { TabNavigationBar(selectedItem) }
    ){
        Navigation(navController = navController, startScreen = startScreen)
    }
}

@Composable
fun TabNavigationBar(selectedItem: Int) {
    var items = listOf("Home", "Tasks", "Quiz", "Settings")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.TaskAlt, Icons.Filled.Quiz, Icons.Filled.Settings)
    val unselectedIcons = listOf(Icons.Outlined.Home,
                                 Icons.Outlined.TaskAlt,
                                 Icons.Outlined.Quiz,
                                 Icons.Outlined.Settings)

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
                onClick = { selectedItem = index }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ProscuttoApp()
}