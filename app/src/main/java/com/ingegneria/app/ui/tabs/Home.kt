package com.ingegneria.app.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.HomeStats
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.ui.screens.PetViewModel
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Home(navController: NavController, petVM: PetViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        TopAppBar(navController)
        HomeStats(navController, petVM.pet);
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar (navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screens.Shop.name) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shop button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {navController.navigate(Screens.Social.name)}
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Social button"
                        )
                    }
                }
            )
        },
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding)
        ) {}
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHome(navController: NavController = rememberNavController()){
    //AppTheme { Home(navController, petVM) }
}