package com.ingegneria.app.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.LoadingDialog


@Composable
fun Settings(navController: NavController) {
    var loading by remember { mutableStateOf(false) }
    val user = Firebase.auth.currentUser
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current user:  ${user?.email}"
            )
            Text(
                text = "Username:  ${user?.displayName}",
                modifier = Modifier. padding(top = 10.dp)
            )
            Button(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                onClick = {
                    loading = true
                    Firebase.auth.signOut()
                    navController.navigate(Screens.Login.name) {
                        popUpTo(0)
                    }
                }
            ) {
                Text("LOGOUT")
            }
        }
    }
    when {
        loading -> {
            LoadingDialog()
        }
    }
}

@Preview
@Composable
fun PreviewSettings(navController: NavController = rememberNavController()){
//    Settings(navController = navController)
}