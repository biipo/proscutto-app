package com.ingegneria.app.ui.tabs

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    /*  TODO: retrieve tasks when app is opened (not when the user open the tasks' tab)
    *   TODO: when a tab is changed the viewModel is cleared, it has to be declared in the Activity (MainActivity) */

    // Used for opening the dialog of a specific task
    var openSingleTaskDialog by remember { mutableStateOf(false) }

    // User for opening the task list dialog
    var openTaskChoiceDialog by remember { mutableStateOf(false) }

    // Used for passing the selected task
    var selectedTask by remember { mutableStateOf(Task()) }

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
            /*
            when { // When the user has selected the task in the task selection dialog the page should update and show the selected tasks
                taskVM.showTasks -> {
                    ShowTaskList(
                        dailyTasks = taskVM.dailyTasks.toList(),
                        weeklyTasks = taskVM.selectedWeeklyTasks,
                        monthlyTasks = taskVM.selectedMonthlyTasks,

                        itemClickedAction = {
                            openSingleTaskDialog = !openSingleTaskDialog
                        },
                        selectItemSaver = { item, group ->
                            selectedTask = item
                        }
                    )
                }
                !taskVM.showTasks -> {
                    Text(
                        text = "Any task has been selected",
                        modifier = Modifier.padding(bottom = 10.dp),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        modifier = Modifier,
                        onClick = {
                            openTaskChoiceDialog = !openTaskChoiceDialog
                        }
                    ) {
                        Text(
                            text = "Choose tasks"
                        )
                    }
                }
            }*/
            val noTasksSelected = taskVM.selectedDailyTasks.isEmpty()
                    && taskVM.selectedWeeklyTasks.isEmpty()
                    && taskVM.selectedMonthlyTasks.isEmpty()

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
                    }
                )
                // Pulsante per scegliere/cambiare task
                Button(
                    modifier = Modifier.padding(top = 10.dp),
                    onClick = { openTaskChoiceDialog = true }
                ) {
                    Text("Change tasks")
                }
            }

        }


        if(openSingleTaskDialog){
                // when the user tap on a task, openDialog is triggered and changed to true so this code open the corresponding dialog
                TaskOptionDialog(
                    onDismissRequest = { openSingleTaskDialog = false },
                    selectedTask = selectedTask
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
    if(tasks.isNotEmpty()) {
        Text(
            text = taskType,
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
    tasks.forEach { item ->
        TaskBox(
            openDialogAction = {
                itemClickedAction()
                selectItemSaver(item, taskType)
            },
            item
        )
    }
}

@Composable
fun ShowTaskList(
    dailyTasks: List<Task>,
    weeklyTasks: List<Task>,
    monthlyTasks: List<Task>,
    itemClickedAction: () -> Unit,
    selectItemSaver: (Task, String) -> Unit,
) {
    // In this composable are passed functions instead of the actual parameters because of the "state hoisting"; in kotlin we CAN'T pass a parameter
    // as reference and CHANGE IT inside a function so we're obligated to pass a lambda function that do that (so we can change
    // variable content from a different scope)

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
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = openDialogAction)
            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .size(200.dp, 80.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if(item.isSelected.value && !item.isCompleted.value) {
                MaterialTheme.colorScheme.primary
            } else if(item.isCompleted.value) {
                MaterialTheme.colorScheme.background
            }else {
                MaterialTheme.colorScheme.secondaryContainer
            },
            contentColor = if(item.isSelected.value && !item.isCompleted.value) {
                MaterialTheme.colorScheme.onPrimary
            } else if(item.isCompleted.value) {
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
fun TaskSelectionDialog(
    onDismissRequest: () -> Unit,
    dailyTasks: List<Task>,
    weeklyTasks: List<Task>,
    monthlyTasks: List<Task>,
    taskVM: TaskViewModel
) {

    val selectedDaily = remember { taskVM.selectedDailyTasks.toMutableStateList() }
    val selectedWeekly = remember { taskVM.selectedWeeklyTasks.toMutableStateList() }
    val selectedMonthly = remember { taskVM.selectedMonthlyTasks.toMutableStateList() }


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
                    Text("Select Your Tasks", style = MaterialTheme.typography.titleLarge)
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
fun TaskOptionDialog(onDismissRequest: () -> Unit, selectedTask: Task) {
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
                    onClick = {
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