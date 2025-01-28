package com.ingegneria.app.ui.tabs

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.ui.screens.PetViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


@Stable
data class Task(
    val title: String = "",
    val desc: String = "",
    private var isCompleted: MutableState<Boolean> = mutableStateOf(false),
    private var isSelected: MutableState<Boolean> = mutableStateOf(false)
) {
    fun toggleSelection() {
        isSelected.value = !isSelected.value
    }
    fun toggleCompletion() {
        isCompleted.value = !isCompleted.value
    }
    fun isTaskCompleted() : Boolean {
        return isCompleted.value
    }
    fun isTaskSelected() : Boolean {
        return isSelected.value
    }
}

class TaskViewModel : ViewModel() {

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

    private var petVM: PetViewModel? = null

    init {
        currentUser?.let { user ->
            // Prima controlliamo se bisogna resettare le tasks
            checkTaskResets(user.uid)
            // Poi carichiamo i dati dal realtime DB (lista completa di tasks)
            retrieveFirebaseData(user.uid, null)
        }

    }

    fun retrieveFirebaseData(userId: String, petVM: PetViewModel?) {
        this.petVM = petVM
        database.child("daily").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dailyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
                // quando ha finito di caricare le daily, carichiamo weekly e monthly:
                loadSelectedTasksIfReady(userId)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errore :( - ${error.message}")
            }
        })
        database.child("weekly").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                weeklyTasks = snapshot.children.mapNotNull {
                    it.getValue(Task::class.java)
                }
                loadSelectedTasksIfReady(userId)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errore :( - ${error.message}")
            }
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

                    val db = firestore.collection("users").document(userId)
                    var cnt = 0
                    // Se è un nuovo giorno, resettiamo le daily tasks
                    if (isNewDay) {
                        db.addSnapshotListener { snapshot, error ->
                            if (error != null) return@addSnapshotListener
                            if (snapshot != null && snapshot.exists()) {
                                val dailyTitles = snapshot.get("selectedDailyTasks") as? Map<String, Boolean> ?: emptyMap()
                                // For each task not completed it deals damage to the pet
                                dailyTitles.forEach { title, isCompleted ->
                                    if (!isCompleted) {
                                        cnt += 1 // counts the number of uncompleted tasks
                                    }
                                }
                            }
                        }
                        db.update(
                                mapOf(
                                    "selectedDailyTasks" to emptyList<String>(),
                                    "lastDailyReset" to FieldValue.serverTimestamp()
                                )
                            )
                        viewModelScope.launch {
                            petVM?.isPetFbInit?.collect { isInitialized ->
                                if (isInitialized) {
                                    petVM!!.petFb!!.takeDamage(cnt * 7)
                                }
                            }
                        }
                    }

                    // Se è una nuova settimana, resettiamo le weekly tasks
                    if (isNewWeek) {
                        db.addSnapshotListener { snapshot, error ->
                            if (error != null) return@addSnapshotListener
                            if (snapshot != null && snapshot.exists()) {
                                val weeklyTitles = snapshot.get("selectedWeeklyTasks") as? Map<String, Boolean> ?: emptyMap()
                                // For each task not completed it deals damage to the pet
                                weeklyTitles.forEach { title, isCompleted ->
                                    if (!isCompleted) {
                                        cnt += 1 // counts the number of uncompleted tasks
                                    }
                                }
                            }
                        }
                        db.update(
                                mapOf(
                                    "selectedWeeklyTasks" to emptyList<String>(),
                                    "lastWeeklyReset" to FieldValue.serverTimestamp()
                                )
                            )
                        viewModelScope.launch {
                            petVM?.isPetFbInit?.collect { isInitialized ->
                                if (isInitialized) {
                                    petVM!!.petFb!!.takeDamage(cnt * 40)
                                }
                            }
                        }
                    }

                    // Se è un nuovo mese, resettiamo le monthly tasks
                    if (isNewMonth) {
                        db.addSnapshotListener { snapshot, error ->
                            if (error != null) return@addSnapshotListener
                            if (snapshot != null && snapshot.exists()) {
                                val monthlyTitles = snapshot.get("selectedMonthlyTasks") as? Map<String, Boolean> ?: emptyMap()
                                // For each task not completed it deals damage to the pet
                                monthlyTitles.forEach { title, isCompleted ->
                                    if (!isCompleted) {
                                        cnt += 1 // counts the number of uncompleted tasks
                                    }
                                }
                            }
                        }
                        db.update(
                                mapOf(
                                    "selectedMonthlyTasks" to emptyList<String>(),
                                    "lastMonthlyReset" to FieldValue.serverTimestamp()
                                )
                            )
                        viewModelScope.launch {
                            petVM?.isPetFbInit?.collect { isInitialized ->
                                if (isInitialized) {
                                    petVM!!.petFb!!.takeDamage(cnt * 100)
                                }
                            }
                        }
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
                    val dailyTitles = snapshot.get("selectedDailyTasks") as? Map<String, Boolean> ?: emptyMap()
                    val weeklyTitles = snapshot.get("selectedWeeklyTasks") as? Map<String, Boolean> ?: emptyMap()
                    val monthlyTitles = snapshot.get("selectedMonthlyTasks") as? Map<String, Boolean> ?: emptyMap()

                    // Filtra e crea un nuovo elenco di Task con isSelected = true
                    val matchedDaily = dailyTasks.filter { dailyTitles.contains(it.title) }
                        .map {
                            it.copy(
                                isSelected = mutableStateOf(true),
                                isCompleted = mutableStateOf(dailyTitles[it.title] ?: false)
                            )
                        }

                    val matchedWeekly = weeklyTasks.filter { weeklyTitles.contains(it.title) }
                        .map {
                            it.copy(
                                isSelected = mutableStateOf(true),
                                isCompleted = mutableStateOf(weeklyTitles[it.title] ?: false)
                            )
                        }

                    val matchedMonthly = monthlyTasks.filter { monthlyTitles.contains(it.title) }
                        .map {
                            it.copy(
                                isSelected = mutableStateOf(true),
                                isCompleted = mutableStateOf(monthlyTitles[it.title] ?: false)
                            )
                        }

                    selectedDailyTasks = matchedDaily
                    selectedWeeklyTasks = matchedWeekly
                    selectedMonthlyTasks = matchedMonthly
                }
            }
    }

    fun setTaskCompleted(taskTitle: String, group: String) {
        currentUser?.uid?.let { id ->
            val db = firestore.collection("users").document(id)
            when(group) {
                "Daily" -> {
                    db.update(
                        mapOf("selectedDailyTasks.$taskTitle" to true)
                    )
                }
                "Weekly" -> {
                    db.update(
                        mapOf("selectedWeeklyTasks.$taskTitle" to true)
                    )
                }
                "Monthly" -> {
                    db.update(
                        mapOf("selectedMonthlyTasks.$taskTitle" to true)
                    )
                }
                else -> {
                    Log.e(
                        "Updating isCompleted on firestore",
                        "Something went wrong, title: $taskTitle of group: $group"
                    )
                }
            }
        }
    }


    fun saveSelectedTasks(
        newDailyTasks: List<Task> = selectedDailyTasks,
        newWeeklyTasks: List<Task> = selectedWeeklyTasks,
        newMonthlyTasks: List<Task> = selectedMonthlyTasks
    ) {
        currentUser?.uid?.let { uid ->
            val dailyTitles = newDailyTasks.associate { it.title to false }
            val weeklyTitles = newWeeklyTasks.associate { it.title to false }
            val monthlyTitles = newMonthlyTasks.associate { it.title to false }
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