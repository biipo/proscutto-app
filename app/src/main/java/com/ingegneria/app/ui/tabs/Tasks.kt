package com.ingegneria.app.ui.tabs

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
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

    // Apertura dialog singola selezione (per “Change task”)
    var openSingleSelectionDialog by remember { mutableStateOf(false) }

    // Apertura dialog multipla selezione (scelta di gruppo)
    var openGroupSelectionDialog by remember { mutableStateOf(false) }


    // Used for opening the dialog of a specific task
    var openSingleTaskDialog by remember { mutableStateOf(false) }

    // User for opening the task list dialog
    var openTaskChoiceDialog by remember { mutableStateOf(false) }

    // Used for passing the selected task
    var selectedTask by remember { mutableStateOf(Task()) }
    var groupOfSelectedTask by remember { mutableStateOf("") }

    // Gruppo di cui voglio scegliere le task (Daily / Weekly / Monthly)
    var groupToChoose by remember { mutableStateOf("") }

    // Retrieve the full task list from firebase db

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val context = LocalContext.current

        LazyColumn (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { PetStats(navController, petVM.pet) }
            item {
                TaskGroupSection(
                    title = "Daily Tasks",
                    selectedTasks = taskVM.selectedDailyTasks,
                    onSelectTask = { task ->
                        // L’utente ha cliccato su una delle daily task
                        if (!task.isTaskCompleted()) {
                            selectedTask = task
                            groupOfSelectedTask = "Daily"
                            openSingleTaskDialog = true
                        } else {
                            Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onChooseClick = {
                        // Apri la selezione multipla per daily
                        groupToChoose = "Daily"
                        openGroupSelectionDialog = true
                    }
                )
            }

            // Sezione per “Weekly Tasks”
            item{TaskGroupSection(
                title = "Weekly Tasks",
                selectedTasks = taskVM.selectedWeeklyTasks,
                onSelectTask = { task ->
                    if (!task.isTaskCompleted()) {
                        selectedTask = task
                        groupOfSelectedTask = "Weekly"
                        openSingleTaskDialog = true
                    } else {
                        Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()
                    }
                },
                onChooseClick = {
                    groupToChoose = "Weekly"
                    openGroupSelectionDialog = true
                }
            )}

            // Sezione per “Monthly Tasks”
            item{TaskGroupSection(
                title = "Monthly Tasks",
                selectedTasks = taskVM.selectedMonthlyTasks,
                onSelectTask = { task ->
                    if (!task.isTaskCompleted()) {
                        selectedTask = task
                        groupOfSelectedTask = "Monthly"
                        openSingleTaskDialog = true
                    } else {
                        Toast.makeText(context, "Task completed", Toast.LENGTH_SHORT).show()
                    }
                },
                onChooseClick = {
                    groupToChoose = "Monthly"
                    openGroupSelectionDialog = true
                }
            )}
        }
    }

    if(openSingleTaskDialog){
        // when the user tap on a task, openDialog is triggered and changed to true so this code open the corresponding dialog
        TaskOptionDialog(
            onDismissRequest = { openSingleTaskDialog = false },
            selectedTask = selectedTask,
            groupOfSelectedTask = groupOfSelectedTask,
            petVM = petVM,
            taskVM = taskVM,
            onChangeTask = {
                openSingleTaskDialog = false
                groupToChoose = groupOfSelectedTask
                openSingleSelectionDialog = true
            }
        )
    }

    // Dialog: singola selezione (per “Change task”)
    if (openSingleSelectionDialog) {
        SingleTaskSelectionDialog(
            onDismissRequest = { openSingleSelectionDialog = false },
            group = groupToChoose,
            taskVM = taskVM,
            onTaskSelected = { newTask ->
                // Esegui la “sostituzione” tra selectedTask e newTask
                taskVM.swapTask(oldTask = selectedTask, newTask = newTask, group = groupToChoose)
                openSingleSelectionDialog = false
            }
        )
    }


    // Dialog: selezione multipla (scegli tot tasks di un gruppo)
    if (openGroupSelectionDialog) {
        GroupTaskSelectionDialog(
            group = groupToChoose,
            onDismissRequest = { openGroupSelectionDialog = false },
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

@Composable
fun TaskGroupSection(
    title: String,
    selectedTasks: List<Task>,
    onSelectTask: (Task) -> Unit,
    onChooseClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
        IconButton(
            onClick = onChooseClick,
            colors = IconButtonColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(Icons.Filled.Add, "Add")
        }
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp),
        thickness = 2.dp,
        color = Color.Black
    )
    if (selectedTasks.isEmpty()) {
        Text(
            text = "No tasks selected for $title",
            modifier = Modifier.padding(bottom = 10.dp),
            fontStyle = FontStyle.Italic
        )
    } else {
        // Mostra l’elenco di task
        selectedTasks.forEach { task ->
            TaskBox(
                openDialogAction = { onSelectTask(task) },
                item = task
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
fun GroupTaskSelectionDialog(
    group: String,
    onDismissRequest: () -> Unit,
    taskVM: TaskViewModel
) {
    val allTasks = taskVM.getUnselectedTasks(group)

    val selectedList = remember(group) {
        when(group) {
            "Daily" -> taskVM.selectedDailyTasks.toMutableStateList()
            "Weekly" -> taskVM.selectedWeeklyTasks.toMutableStateList()
            "Monthly" -> taskVM.selectedMonthlyTasks.toMutableStateList()
            else -> mutableStateListOf()
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = RoundedCornerShape(10.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select $group tasks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(allTasks.size) { index ->
                        val item = allTasks[index]
                        val isSelected = selectedList.contains(item)
                        SelectableTaskBox(
                            task = item,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedList.remove(item)
                                } else {
                                    selectedList.add(item)
                                }
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        // Salviamo la nuova lista nel TaskViewModel
                        when(group) {
                            "Daily" -> {
                                taskVM.saveSelectedTasks(
                                    newDailyTasks = selectedList,
                                    newWeeklyTasks = taskVM.selectedWeeklyTasks,
                                    newMonthlyTasks = taskVM.selectedMonthlyTasks
                                )
                            }
                            "Weekly" -> {
                                taskVM.saveSelectedTasks(
                                    newDailyTasks = taskVM.selectedDailyTasks,
                                    newWeeklyTasks = selectedList,
                                    newMonthlyTasks = taskVM.selectedMonthlyTasks
                                )
                            }
                            "Monthly" -> {
                                taskVM.saveSelectedTasks(
                                    newDailyTasks = taskVM.selectedDailyTasks,
                                    newWeeklyTasks = taskVM.selectedWeeklyTasks,
                                    newMonthlyTasks = selectedList
                                )
                            }
                        }
                        onDismissRequest()
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}



@Composable
fun SingleTaskSelectionDialog(
    onDismissRequest: () -> Unit,
    group: String,
    taskVM: TaskViewModel,
    onTaskSelected: (Task) -> Unit
) {

    val unselectedTasks = taskVM.getUnselectedTasks(group)

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = RoundedCornerShape(8.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose a new $group task",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(unselectedTasks.size) { index ->
                        val item = unselectedTasks[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    // Al click, chiudiamo e restituiamo la task scelta
                                    onTaskSelected(item)
                                },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
                Button(onClick = onDismissRequest) {
                    Text("Cancel")
                }
            }
        }
    }
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
    taskVM: TaskViewModel,
    onChangeTask: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val context = LocalContext.current
        Card (
            modifier = Modifier.wrapContentSize(),
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
                    onClick = {onChangeTask()},
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