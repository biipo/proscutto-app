package com.ingegneria.app.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingegneria.app.models.PetFirebaseSync
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.HomeStats
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.MascotImageBig
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Home(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        TopAppBar(navController)
        HomeStats(navController, petVM.pet);
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar () {
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
    AppTheme { Home(navController) }
}