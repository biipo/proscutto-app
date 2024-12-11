package com.ingegneria.app.ui.screens

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    val title: String = "",
    val desc: String = ""
)

@Composable
fun Tasks(navController: NavController) {

    // TODO: retreive tasks when app is opened
    val database = FirebaseDatabase.getInstance().reference.child("task")
    // The task list is a Map with key (daily, weekly or monthly) and value (list of Task object associated in firebase)
    /*
        taskList = {
            "daily" : [
                Task(title = "...", desc = "...")
                ...
            ],
            "weekly" : [
                Task(title = "...", desc = "...")
                ...
            ],
            "monthly" : [
                Task(title = "...", desc = "...")
                ...
            ],
        }
     */
    var dailyTasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var weeklyTasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var monthlyTasks by remember { mutableStateOf<List<Task>>(emptyList()) }

    var openTaskDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf(Task()) }
    /* TODO: create a list of selected tasks for each category,
        then for each user save the id of the task selected for each type,
        then in the user space we mark the completed tasks,
        if the user want to change a task we load a tmp list of the other (based on a set and then casted into a list??)
     */
    LaunchedEffect(Unit) {
        database.child("daily").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dailyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
        database.child("weekly").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                weeklyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
        database.child("monthly").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                monthlyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errorino :( - ${error.message}")
            }
        })
    }


    /*
       TODO: set page default in which the user choose the task, then create also
            page in which the user can change it (by opening the dialog)
       TODO: save user selected and user finished tasks
     */
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
                            .padding(start = 8.dp, end = 8.dp),
                        thickness = 2.dp,
                        color = Color.Red
                    )
                }
            }
        ){ padding ->

            LazyColumn (
                modifier = Modifier.padding(top = 150.dp)
            ){
                item {
                    Text(
                        text = "Daily",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 5.dp, bottom = 2.dp),
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 8.dp, end = 10.dp),
                        thickness = 2.dp,
                        color = Color.Black
                    )
                }
                itemsIndexed(dailyTasks) { index, item ->
                    taskBox(openDialogAction = {
                        openTaskDialog = !openTaskDialog
                        selectedTask = item
                    }, item.title)
                }
                item {
                    Text(
                        text = "Weekly",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 5.dp, bottom = 2.dp, top = 5.dp),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 8.dp, end = 10.dp),
                        thickness = 2.dp,
                        color = Color.Black
                    )
                }
                itemsIndexed(weeklyTasks) { index, item ->
                    taskBox(openDialogAction = {
                        openTaskDialog = !openTaskDialog
                        selectedTask = item
                    }, item.title)
                }
                item {
                    Text(
                        text = "Monthly",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 5.dp, bottom = 2.dp, top = 5.dp),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 8.dp, end = 10.dp),
                        thickness = 2.dp,
                        color = Color.Black
                    )
                }
                itemsIndexed(monthlyTasks) { index, item ->
                    taskBox(openDialogAction = {
                        openTaskDialog = !openTaskDialog
                        selectedTask = item
                    }, item.title)
                }
            }

            when {
                openTaskDialog -> {
                    taskOptionDialog(onDismissRequest = {openTaskDialog = false}, selectedTask)
                }
            }
        }

    }
}

@Composable
fun taskBox(openDialogAction: () -> Unit, item: String) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = openDialogAction)
            .fillMaxWidth()
            .size(200.dp, 80.dp),
        shape = MaterialTheme.shapes.medium,
//        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
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

@Composable
fun taskSelectionDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(500.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // TODO: display task list (of the selected category)
            }
        }
    }
}

@Composable
fun taskOptionDialog(onDismissRequest: () -> Unit, selectedTask: Task) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(500.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = selectedTask.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(bottom = 5.dp, top = 40.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 10.dp),
                    thickness = 1.dp,
                    color = Color.Black
                )
                Text(
                    text = selectedTask.desc,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 40.dp, start = 10.dp, end = 10.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier
                        .padding(end = 20.dp, start = 20.dp)
                        .size(350.dp, 50.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(end = 20.dp, start = 20.dp, top = 20.dp)
                        .size(350.dp, 50.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Completed with a friend",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Button(
                    modifier = Modifier
                        .padding(end = 20.dp, start = 20.dp, top = 20.dp, bottom = 50.dp)
                        .size(350.dp, 50.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Change task",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
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