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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Stats(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()

    // Stati locali in cui salviamo i valori letti da Firestore
    var maxLevelReached by remember { mutableStateOf(1) }
    var dailyTasksCompleted by remember { mutableStateOf(0) }
    var weeklyTasksCompleted by remember { mutableStateOf(0) }
    var monthlyTasksCompleted by remember { mutableStateOf(0) }
    var quizCompleted by remember { mutableStateOf(0) }
    var totalLoginDays by remember { mutableStateOf(0) }

    // Quando questo composable viene caricato, se c'è un utente loggato,
    // facciamo un addSnapshotListener per tenere i valori sincronizzati.
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            firestore.collection("users")
                .document(user.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error == null && snapshot != null && snapshot.exists()) {
                        maxLevelReached = snapshot.getLong("maxLevelReached")?.toInt() ?: 1
                        dailyTasksCompleted = snapshot.getLong("dailyTasksCompleted")?.toInt() ?: 0
                        weeklyTasksCompleted = snapshot.getLong("weeklyTasksCompleted")?.toInt() ?: 0
                        monthlyTasksCompleted = snapshot.getLong("monthlyTasksCompleted")?.toInt() ?: 0
                        quizCompleted = snapshot.getLong("quizCompleted")?.toInt() ?: 0
                        totalLoginDays = snapshot.getLong("totalLoginDays")?.toInt() ?: 0
                    }
                }
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TextButton(
                modifier = Modifier
                    .padding(start = 7.dp, top = 10.dp)
                    .wrapContentSize(),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                )
            }
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = "Stats Page", modifier = Modifier.padding(bottom = 20.dp))

            Text(text = "Max Level Reached: $maxLevelReached")
            Text(text = "Daily Tasks Completed: $dailyTasksCompleted")
            Text(text = "Weekly Tasks Completed: $weeklyTasksCompleted")
            Text(text = "Monthly Tasks Completed: $monthlyTasksCompleted")
            Text(text = "Quiz Completed: $quizCompleted")
            Text(text = "Days Logged In: $totalLoginDays")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsPreview(navController: NavController = rememberNavController()) {
    AppTheme { Stats(navController = navController) }
}
