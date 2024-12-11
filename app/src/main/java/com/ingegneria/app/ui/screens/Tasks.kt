package com.ingegneria.app.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Task(
    val desc: String = ""
)


@Composable
fun Tasks(navController: NavController) {
    val database = FirebaseDatabase.getInstance().reference.child("task")
    var dailyTaskList by remember { mutableStateOf<List<String>>(emptyList()) }
    var weeklyTaskList by remember { mutableStateOf<List<String>>(emptyList()) }
    var monthlyTaskList by remember { mutableStateOf<List<String>>(emptyList()) }

    var openTaskDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        database.child("daily").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
                dailyTaskList = taskList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
        database.child("weekly").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
                weeklyTaskList = taskList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
        database.child("monthly").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
                monthlyTaskList = taskList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold (
            topBar = {
                Column {
                    CharacterStatsTask()
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 7.dp, bottom = 10.dp, end = 7.dp),
                        thickness = 2.dp,
                        color = Color.Red
                    )
                }
            }
        ){ padding ->

            LazyColumn (
                modifier = Modifier.padding(top = 130.dp)
            ){
                itemsIndexed(dailyTaskList + weeklyTaskList + monthlyTaskList) { index, item ->
                    if(index == 5 || index == 10) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(start = 10.dp, top = 15.dp, bottom = 8.dp, end = 10.dp),
                            thickness = 2.dp,
                            color = Color.Black
                        )
                    }
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable(onClick = { openTaskDialog = !openTaskDialog})
                            .fillMaxWidth()
                            .size(200.dp, 80.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, Color.Black),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                     ){
                        Row (
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = item,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            when {
                openTaskDialog -> {
                    taskChoiceDialog(onDismissRequest = {openTaskDialog = false})
                }
            }
        }

    }
}

@Composable
fun taskChoiceDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ){
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                text = "Prova"
            )
        }
    }
}

@Composable
fun CharacterStatsTask() {
    val lvlExample = 12
    val currentHpExample = 100
    val currentXpExample = 140
    val maxHp = 300
    val maxXp = 200

    Column {
        Row(
            modifier = Modifier
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$lvlExample",
                    fontSize = 50.sp
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "$currentHpExample/$maxHp",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.End)
                )
                LinearProgressIndicator(
                    progress = { (currentHpExample / maxHp.toFloat()) },
                    modifier = Modifier.fillMaxWidth().height(15.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "$currentXpExample/$maxXp",
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.End)
                        .padding(0.dp, 15.dp, 0.dp, 0.dp)
                )
                LinearProgressIndicator(
                    progress = { (currentXpExample / maxXp.toFloat()) },
                    modifier = Modifier.fillMaxWidth().height(15.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTasks(navController: NavController = rememberNavController()){
    Tasks(navController = navController)
}