package com.ingegneria.app.ui.otherpages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Social(navController: NavController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TextButton(
                modifier = Modifier
                    .padding(start = 7.dp, top = 10.dp)
                    .wrapContentSize(),
                onClick = {navController.navigate(Screens.Home.name)}
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                )
            }
        }
    ){ padding ->
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Social page"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialPreview(navController: NavController = rememberNavController()) {
    AppTheme { Social(navController = navController) }
}
