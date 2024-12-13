package com.ingegneria.app.ui.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Stable
data class Task(
    val title: String = "",
    val desc: String = "",
    var isCompleted: MutableState<Boolean> = mutableStateOf(false),
    var isSelected: MutableState<Boolean> = mutableStateOf(false)
) {
    fun toggleSelection() {
        isSelected.value = !isSelected.value
    }
    fun toggleCompletion() {
        isCompleted.value = !isCompleted.value
    }
}

class TaskViewModel : ViewModel() {

    // TODO: maybe set some fields private??

    val database = FirebaseDatabase.getInstance().reference.child("task")
    var dailyTasks by mutableStateOf<List<Task>>(emptyList())
    var weeklyTasks by mutableStateOf<List<Task>>(emptyList())
    var monthlyTasks by mutableStateOf<List<Task>>(emptyList())

    var userDailyTasks: MutableSet<Task> = mutableSetOf()
    var userWeeklyTasks: MutableSet<Task> = mutableSetOf()
    var userMonthlyTasks: MutableSet<Task> = mutableSetOf()

    var showTasks: Boolean = false

    fun retrieveFirebaseData() {
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

    fun toggleShowTasks() {
        showTasks = !showTasks
    }
}
/*

//            val scope = rememberCoroutineScope()
//            val snackbarHostState = remember { SnackbarHostState() }
//            scope.launch {
//                snackbarHostState.showSnackbar("the condition is : ${showingCondition}")
//            }
//            SnackbarHost(
//                modifier = Modifier.padding(top = 20.dp),
//                hostState = snackbarHostState
//            )

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
 */