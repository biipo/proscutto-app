package com.ingegneria.app.ui.tabs

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Settings(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
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
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),
                onClick = {
                    showDialog = true
                }
            ) {
                Text("DELETE ACCOUNT")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    val user = Firebase.auth.currentUser!!
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User account deleted.")
                            }
                        }
                }) {
                    Text("CONFIRM")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CANCEL")
                }
            },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") }
        )
    }
}

@Preview
@Composable
fun PreviewSettings(navController: NavController = rememberNavController()) {
    Settings(navController = navController)
}
