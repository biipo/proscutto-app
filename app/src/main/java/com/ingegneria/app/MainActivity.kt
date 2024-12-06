package com.ingegneria.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ingegneria.app.navigation.Navigation
import com.ingegneria.app.ui.theme.AppTheme
import com.ingegneria.app.navigation.Screens

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
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val startScreen =
        if (firebaseUser == null) {
            Screens.Login.name
        } else {
            Screens.Home.name
        }
    Navigation(navController = navController, startScreen = startScreen)
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ProscuttoApp()
}