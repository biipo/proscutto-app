package com.ingegneria.app.ui.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date


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
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    val database = FirebaseDatabase.getInstance().reference.child("task")
    var dailyTasks by mutableStateOf<List<Task>>(emptyList())
    var weeklyTasks by mutableStateOf<List<Task>>(emptyList())
    var monthlyTasks by mutableStateOf<List<Task>>(emptyList())

    var selectedDailyTasks by mutableStateOf<List<Task>>(emptyList())
    var selectedWeeklyTasks by mutableStateOf<List<Task>>(emptyList())
    var selectedMonthlyTasks by mutableStateOf<List<Task>>(emptyList())

    var showTasks = false

    init {
        currentUser?.let { user ->
            // Prima controlliamo se bisogna resettare le tasks
            checkTaskResets(user.uid)
            // Poi carichiamo i dati dal realtime DB (lista completa di tasks)
            retrieveFirebaseData(user.uid)
        }
    }

    fun retrieveFirebaseData(userId: String) {
        database.child("daily").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dailyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
                // quando ha finito di caricare le daily, carichiamo weekly e monthly:
                loadSelectedTasksIfReady(userId)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        database.child("weekly").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                weeklyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
                loadSelectedTasksIfReady(userId)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        database.child("monthly").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                monthlyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
                loadSelectedTasksIfReady(userId)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun loadSelectedTasksIfReady(userId: String) {
        // Se almeno daily/weekly/monthly sono stati caricati carica quello che deve
        if (dailyTasks.isNotEmpty() && weeklyTasks.isNotEmpty() && monthlyTasks.isNotEmpty()) {
            loadSelectedTasks(userId)
        }
    }

    private fun checkTaskResets(userId: String) {
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val lastDailyReset = document.getTimestamp("lastDailyReset")?.toDate()
                    val lastWeeklyReset = document.getTimestamp("lastWeeklyReset")?.toDate()
                    val lastMonthlyReset = document.getTimestamp("lastMonthlyReset")?.toDate()

                    val now = Date()

                    val isNewDay = isNextDay(lastDailyReset, now)
                    val isNewWeek = isNextWeek(lastWeeklyReset, now)
                    val isNewMonth = isNextMonth(lastMonthlyReset, now)

                    // Se è un nuovo giorno, resettiamo le daily tasks
                    if (isNewDay) {
                        firestore.collection("users")
                            .document(userId)
                            .update(
                                mapOf(
                                    "selectedDailyTasks" to emptyList<String>(),
                                    "lastDailyReset" to FieldValue.serverTimestamp()
                                )
                            )
                    }

                    // Se è una nuova settimana, resettiamo le weekly tasks
                    if (isNewWeek) {
                        firestore.collection("users")
                            .document(userId)
                            .update(
                                mapOf(
                                    "selectedWeeklyTasks" to emptyList<String>(),
                                    "lastWeeklyReset" to FieldValue.serverTimestamp()
                                )
                            )
                    }

                    // Se è un nuovo mese, resettiamo le monthly tasks
                    if (isNewMonth) {
                        firestore.collection("users")
                            .document(userId)
                            .update(
                                mapOf(
                                    "selectedMonthlyTasks" to emptyList<String>(),
                                    "lastMonthlyReset" to FieldValue.serverTimestamp()
                                )
                            )
                    }
                    // Dopo aver gestito eventuali reset, recuperiamo le task effettivamente selezionate sul db
                    loadSelectedTasks(userId)
                }
            }
    }

    private fun loadSelectedTasks(userId: String) {
        firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    val dailyTitles = snapshot.get("selectedDailyTasks") as? List<String> ?: emptyList()
                    val weeklyTitles = snapshot.get("selectedWeeklyTasks") as? List<String> ?: emptyList()
                    val monthlyTitles = snapshot.get("selectedMonthlyTasks") as? List<String> ?: emptyList()

                    // Filtra e crea un nuovo elenco di Task con isSelected = true
                    val matchedDaily = dailyTasks.filter { dailyTitles.contains(it.title) }
                        .map { it.copy(isSelected = mutableStateOf(true)) }

                    val matchedWeekly = weeklyTasks.filter { weeklyTitles.contains(it.title) }
                        .map { it.copy(isSelected = mutableStateOf(true)) }

                    val matchedMonthly = monthlyTasks.filter { monthlyTitles.contains(it.title) }
                        .map { it.copy(isSelected = mutableStateOf(true)) }

                    selectedDailyTasks = matchedDaily
                    selectedWeeklyTasks = matchedWeekly
                    selectedMonthlyTasks = matchedMonthly
                }
            }
    }

    fun saveSelectedTasks(
        newDailyTasks: List<Task> = selectedDailyTasks,
        newWeeklyTasks: List<Task> = selectedWeeklyTasks,
        newMonthlyTasks: List<Task> = selectedMonthlyTasks
    ) {
        currentUser?.uid?.let { uid ->
            val dailyTitles = newDailyTasks.map { it.title }
            val weeklyTitles = newWeeklyTasks.map { it.title }
            val monthlyTitles = newMonthlyTasks.map { it.title }
            firestore.collection("users").document(uid).update(
                mapOf(
                    "selectedDailyTasks" to dailyTitles,
                    "selectedWeeklyTasks" to weeklyTitles,
                    "selectedMonthlyTasks" to monthlyTitles
                )
            )
                .addOnSuccessListener {
                    // Aggiorniamo lo stato locale
                    selectedDailyTasks = newDailyTasks
                    selectedWeeklyTasks = newWeeklyTasks
                    selectedMonthlyTasks = newMonthlyTasks
                }
        }
    }

    private fun isNextDay(lastDate: Date?, currentDate: Date): Boolean {
        if (lastDate == null) return true
        val calLast = Calendar.getInstance().apply { time = lastDate }
        val calNow = Calendar.getInstance().apply { time = currentDate }
        return calLast.get(Calendar.DAY_OF_YEAR) != calNow.get(Calendar.DAY_OF_YEAR) ||
                calLast.get(Calendar.YEAR) != calNow.get(Calendar.YEAR)
    }

    private fun isNextWeek(lastDate: Date?, currentDate: Date): Boolean {
        if (lastDate == null) return true
        val calLast = Calendar.getInstance().apply { time = lastDate }
        val calNow = Calendar.getInstance().apply { time = currentDate }
        val lastWeek = calLast.get(Calendar.WEEK_OF_YEAR)
        val currentWeek = calNow.get(Calendar.WEEK_OF_YEAR)
        val lastYear = calLast.get(Calendar.YEAR)
        val currentYear = calNow.get(Calendar.YEAR)

        return (lastWeek != currentWeek) || (lastYear != currentYear)
    }

    private fun isNextMonth(lastDate: Date?, currentDate: Date): Boolean {
        if (lastDate == null) return true
        val calLast = Calendar.getInstance().apply { time = lastDate }
        val calNow = Calendar.getInstance().apply { time = currentDate }
        val lastMonth = calLast.get(Calendar.MONTH)
        val currentMonth = calNow.get(Calendar.MONTH)
        val lastYear = calLast.get(Calendar.YEAR)
        val currentYear = calNow.get(Calendar.YEAR)

        return (lastMonth != currentMonth) || (lastYear != currentYear)
    }


    fun toggleShowTasks() {
        showTasks = !showTasks
    }
}