package com.ingegneria.app.ui.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.ui.common.PetStats
import com.ingegneria.app.ui.screens.PetViewModel

@Composable
fun Tasks(navController: NavController, taskVM: TaskViewModel, petVM: PetViewModel) {

    // Used for opening the dialog of a specific task
    var openSingleTaskDialog by remember { mutableStateOf(false) }

    // User for opening the task list dialog
    var openTaskChoiceDialog by remember { mutableStateOf(false) }

    // Used for passing the selected task
    var selectedTask by remember { mutableStateOf(Task()) }
    var groupOfSelectedTask by remember { mutableStateOf("") }

    // Retrieve the full task list from firebase db

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            PetStats(navController, petVM.pet)
            HorizontalDivider(
                modifier = Modifier
                    .padding(10.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            val noTasksSelected = taskVM.selectedDailyTasks.isEmpty()
                    && taskVM.selectedWeeklyTasks.isEmpty()
                    && taskVM.selectedMonthlyTasks.isEmpty()
            Log.e("Selected TASKS", "No selected statement is $noTasksSelected")

            if (noTasksSelected) {
                Text(
                    text = "No tasks selected",
                    modifier = Modifier.padding(bottom = 10.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { openTaskChoiceDialog = true }
                ) {
                    Text("Choose tasks")
                }
            } else {
                // Se ci sono task selezionate, le mostriamo
                ShowTaskList(
                    dailyTasks = taskVM.selectedDailyTasks,
                    weeklyTasks = taskVM.selectedWeeklyTasks,
                    monthlyTasks = taskVM.selectedMonthlyTasks,
                    itemClickedAction = {
                        openSingleTaskDialog = true
                    },
                    selectItemSaver = { item, group ->
                        selectedTask = item
                        groupOfSelectedTask = group
                    }
                )
                // Pulsante per scegliere/cambiare task (messo per ciascun gruppo)
//                Button(
//                    modifier = Modifier.padding(top = 10.dp),
//                    onClick = { openTaskChoiceDialog = true }
//                ) {
//                    Text("Change tasks")
//                }
            }

        }


        if(openSingleTaskDialog){
                // when the user tap on a task, openDialog is triggered and changed to true so this code open the corresponding dialog
                TaskOptionDialog(
                    onDismissRequest = { openSingleTaskDialog = false },
                    selectedTask = selectedTask,
                    groupOfSelectedTask = groupOfSelectedTask,
                    petVM = petVM,
                    taskVM = taskVM
                )
            }

        if(openTaskChoiceDialog){
                // when the user tap on the button relative for task choice
                TaskSelectionDialog(
                    onDismissRequest =  { openTaskChoiceDialog = false },
                    dailyTasks = taskVM.dailyTasks,
                    weeklyTasks = taskVM.weeklyTasks,
                    monthlyTasks = taskVM.monthlyTasks,
                    taskVM = taskVM
                )

            }
        }
}


@Composable
fun ShowSingleGroupList(
    taskType: String,
    tasks: List<Task>,
    itemClickedAction: () -> Unit,
    selectItemSaver: (Task, String) -> Unit
) {
    Text(
        text = taskType,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 10.dp, bottom = 2.dp),
    )
    HorizontalDivider(
        modifier = Modifier
            .padding(start = 10.dp, bottom = 8.dp, end = 10.dp),
        thickness = 2.dp,
        color = Color.Black
    )
    if(tasks.isNotEmpty()) {
        tasks.forEach { item ->
            TaskBox(
                openDialogAction = {
                    itemClickedAction()
                    selectItemSaver(item, taskType)
                },
                item
            )
        }
    } else {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {
                /* TODO: Open a dialog with only the tasks of the current group */
                },
                modifier = Modifier.wrapContentSize().padding(vertical = 5.dp)
            ) {
                Text ("Select tasks")
            }
        }
    }
}

@Composable
fun ShowTaskList(
    dailyTasks: List<Task>,
    weeklyTasks: List<Task>,
    monthlyTasks: List<Task>,
    itemClickedAction: () -> Unit,
    selectItemSaver: (Task, String) -> Unit
) {
    LazyColumn {
        item {
            ShowSingleGroupList("Daily", dailyTasks, itemClickedAction, selectItemSaver)
            ShowSingleGroupList("Weekly", weeklyTasks, itemClickedAction, selectItemSaver)
            ShowSingleGroupList("Monthly", monthlyTasks, itemClickedAction, selectItemSaver)
        }
    }
}

@Composable
fun TaskBox(openDialogAction: () -> Unit, item: Task) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = {
                if (!item.isTaskCompleted()) {
                    openDialogAction()
                } else {
                    Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()
                }
            })
            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .size(200.dp, 80.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if(item.isTaskSelected() && !item.isTaskCompleted()) {
                MaterialTheme.colorScheme.primary
            } else if(item.isTaskCompleted()) {
                MaterialTheme.colorScheme.background
            }else {
                MaterialTheme.colorScheme.secondaryContainer
            },
            contentColor = if(item.isTaskSelected() && !item.isTaskCompleted()) {
                MaterialTheme.colorScheme.onPrimary
            } else if(item.isTaskCompleted()) {
                MaterialTheme.colorScheme.onBackground
            }else {
                MaterialTheme.colorScheme.onSecondaryContainer
            }
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
                text = item.title,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SingleTaskSelectionDialog(
    onDismissRequest: () -> Unit,
    tasks: List<Task>,
    taskVM: TaskViewModel,
) {
    // TODO: show the other tasks
}

@Composable
fun TaskSelectionDialog(
    onDismissRequest: () -> Unit,
    dailyTasks: List<Task>,
    weeklyTasks: List<Task>,
    monthlyTasks: List<Task>,
    taskVM: TaskViewModel,
) {

    val selectedDaily = remember { taskVM.selectedDailyTasks.toMutableStateList() }
    val selectedWeekly = remember { taskVM.selectedWeeklyTasks.toMutableStateList() }
    val selectedMonthly = remember { taskVM.selectedMonthlyTasks.toMutableStateList() }

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    Text("Select Your Tasks (2 for each)", style = MaterialTheme.typography.titleLarge)
                }
                item {
                    Text("Daily Tasks", style = MaterialTheme.typography.titleMedium)
                    dailyTasks.forEach { item ->
                        SelectableTaskBox(
                            task = item,
                            isSelected = selectedDaily.contains(item),
                            onClick = {
                                if (selectedDaily.contains(item)) {
                                    selectedDaily.remove(item)
                                } else {
                                    selectedDaily.add(item)
                                }
                            }
                        )
                    }
                }

                item {
                    Text("Weekly Tasks", style = MaterialTheme.typography.titleMedium)
                    weeklyTasks.forEach { item ->
                        SelectableTaskBox(
                            task = item,
                            isSelected = selectedWeekly.contains(item),
                            onClick = {
                                if (selectedWeekly.contains(item)) {
                                    selectedWeekly.remove(item)
                                } else {
                                    selectedWeekly.add(item)
                                }
                            }
                        )
                    }
                }

                item {
                    Text("Monthly Tasks", style = MaterialTheme.typography.titleMedium)
                    monthlyTasks.forEach { item ->
                        SelectableTaskBox(
                            task = item,
                            isSelected = selectedMonthly.contains(item),
                            onClick = {
                                if (selectedMonthly.contains(item)) {
                                    selectedMonthly.remove(item)
                                } else {
                                    selectedMonthly.add(item)
                                }
                            }
                        )
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(width = 140.dp, height = 40.dp),
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(
                        text = "Cancel"
                    )
                }
                Button(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(width = 140.dp, height = 40.dp),
                    onClick = {
                        /* TODO: maybe it's better to scan the list and keep only the tasks with isSelected == true?
                            rather than add and remove them immediately when clicked */
                        if (selectedDaily.size < 2 || selectedWeekly.size < 2 || selectedMonthly.size < 2) {
                            Toast.makeText(context, "Select at least 2 tasks for each group", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        taskVM.saveSelectedTasks(
                            newDailyTasks = selectedDaily,
                            newWeeklyTasks = selectedWeekly,
                            newMonthlyTasks = selectedMonthly
                        )
                        onDismissRequest()
                      },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            }
        }
    }
}

@Composable
fun SelectableTaskBox(
    task: Task,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = task.title,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun TaskOptionDialog(
    onDismissRequest: () -> Unit,
    selectedTask: Task,
    groupOfSelectedTask: String,
    petVM: PetViewModel,
    taskVM: TaskViewModel
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val context = LocalContext.current
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
                    onClick = {
                        when (groupOfSelectedTask) {
                            "Daily" -> {
                                petVM.petFb?.gainXp(4)
                                petVM.petFb?.heal(4)
                            }
                            "Weekly" -> {
                                petVM.petFb?.gainXp(20)
                                petVM.petFb?.heal(20)
                            }
                            "Monthly" -> {
                                petVM.petFb?.gainXp(50)
                                petVM.petFb?.heal(50)
                            }
                            else -> {
                                Toast.makeText(
                                    context,
                                    "There has been a problem, try again later",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        taskVM.setTaskCompleted(selectedTask.title, groupOfSelectedTask)
                        selectedTask.toggleCompletion()
                        onDismissRequest()
                    },
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
                    onClick = {}, // TODO: complete but with a friend
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

@Preview(showBackground = true)
@Composable
fun PreviewTasks(navController: NavController = rememberNavController()){
}