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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun Settings(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight(),
        ) {
            Text(
                text = "Current user:  ${Firebase.auth.currentUser?.email}"
            )
            Button(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),
                onClick = {
                    Firebase.auth.signOut()
                }
            ) {
                Text("LOGOUT")
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettings(navController: NavController = rememberNavController()){
    Settings(navController = navController)
}