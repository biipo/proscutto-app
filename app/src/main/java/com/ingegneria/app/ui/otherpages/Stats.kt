package com.ingegneria.app.ui.otherpages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                onClick = {navController.popBackStack()}
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
        ){
            Column (
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Top,
            ){
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Statistics",
                    style = MaterialTheme.typography.displayMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            LazyColumn (
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center
            ){
                item {
                    StatCard("Max Level Reached", maxLevelReached.toString())
                }
                item {
                    StatCard("Daily Tasks Completed", dailyTasksCompleted.toString())
                }
                item {
                    StatCard("Weekly Tasks Completed", weeklyTasksCompleted.toString())
                }
                item {
                    StatCard("Monthly Tasks Completed", monthlyTasksCompleted.toString())
                }
                item {
                    StatCard("Quiz Completed", quizCompleted.toString())
                }
                item {
                    StatCard("Days Logged In", totalLoginDays.toString())
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
            Card(
                modifier = Modifier.padding(end = 5.dp).wrapContentSize(),
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = value,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsPreview(navController: NavController = rememberNavController()) {
    AppTheme { Stats(navController = navController) }
}
